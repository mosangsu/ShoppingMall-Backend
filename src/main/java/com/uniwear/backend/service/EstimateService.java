package com.uniwear.backend.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.uniwear.backend.dto.EstimateDto;
import com.uniwear.backend.dto.Response;
import com.uniwear.backend.dto.EstimateDto.Req;
import com.uniwear.backend.dto.EstimateDto.Req.EstimatePrintDto;
import com.uniwear.backend.dto.EstimateDto.Req.EstimatePrintPictureDto;
import com.uniwear.backend.dto.EstimateDto.Req.EstimateProductDto;
import com.uniwear.backend.dto.EstimateDto.Req.EstimateProductGroupDto;
import com.uniwear.backend.entity.Color;
import com.uniwear.backend.entity.Estimate;
import com.uniwear.backend.entity.EstimatePrint;
import com.uniwear.backend.entity.EstimatePrintColor;
import com.uniwear.backend.entity.EstimatePrintPicture;
import com.uniwear.backend.entity.EstimateProduct;
import com.uniwear.backend.entity.EstimateProductGroup;
import com.uniwear.backend.entity.EstimateProductOption;
import com.uniwear.backend.entity.Picture;
import com.uniwear.backend.entity.ProductOption;
import com.uniwear.backend.enums.EstimateStatus;
import com.uniwear.backend.repository.CartRepository;
import com.uniwear.backend.repository.ColorRepository;
import com.uniwear.backend.repository.EstimatePrintRepository;
import com.uniwear.backend.repository.EstimateProductGroupRepository;
import com.uniwear.backend.repository.EstimateProductRepository;
import com.uniwear.backend.repository.EstimateRepository;
import com.uniwear.backend.repository.MemberRepository;
import com.uniwear.backend.repository.PictureRepository;
import com.uniwear.backend.repository.PrintPositionRepository;
import com.uniwear.backend.repository.PrintSizeRepository;
import com.uniwear.backend.repository.PrintTypeRepository;
import com.uniwear.backend.repository.ProductOptionRepository;
import com.uniwear.backend.repository.ProductRepository;
import com.uniwear.backend.repository.jpql.JEstimateRepository;
import com.uniwear.backend.util.FileUtil;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class EstimateService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileUtil fileUtil;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private EstimateProductRepository estimateProductRepository;

    @Autowired
    private EstimateProductGroupRepository estimateProductGroupRepository;

    @Autowired
    private EstimatePrintRepository estimatePrintRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private PrintPositionRepository printPositionRepository;
    
    @Autowired
    private PrintTypeRepository printTypeRepository;
    
    @Autowired
    private PrintSizeRepository printSizeRepository;

    @Autowired
    private JEstimateRepository jEstimateRepository;

    @Autowired
    private PictureRepository pictureRepository;

    public List<EstimateDto.Res> getEstimateList(Long memberId) {
        // return estimateRepository.findAllByMemberMemberIdOrderByCreatedAtDesc(memberId).stream().map(p -> modelMapper.map(p, EstimateDto.Res.class))
                // .collect(Collectors.toList());
        return jEstimateRepository.findAll(memberId).stream().map(p -> modelMapper.map(p, EstimateDto.Res.class)).collect(Collectors.toList());
    }

    public List<EstimateDto.Count.Res> getEstimateListCount(Long memberId) {
        List<EstimateDto.Count.Res> result = jEstimateRepository.countEstimateGroupByStatus(memberId);
        return result;
    }

    /**
     * 견적문의 저장 기능
     * 연관관계가 복잡하게 엮어있는 테이블을 저장하므로 modelMapper를 사용하지 않고 직접 DTO->Entity를 구현함 
     * @param memberId 유저ID
     * @param files 첨부파일
     * @param data 견적문의 데이터
     * @param response 응답값에 estimateId 전달용
     * @return
     */
    public boolean insertEstimate(Long memberId, List<MultipartFile> files, EstimateDto.Req estimateDto, Response response) {
        // 파일이름(클라이언트에서 uuid로 넘겨준)을 id값으로 한 Map 객체 생성
        Map<String, MultipartFile> fileMap = null;
        if (files != null) fileMap = files.stream().collect(Collectors.toMap(MultipartFile::getOriginalFilename, item -> item));
        else fileMap = new HashMap<String, MultipartFile>();
        
        Estimate estimate = EstimateDto.Req.toEntity(estimateDto);
        if (memberId != null) estimate.setMember(memberRepository.getById(memberId));
        if (estimateDto.isTempYn())
            estimate.setStatus(EstimateStatus.BEFORE_SUBMISSION);
        else
            estimate.setStatus(EstimateStatus.ESTIMATE_IN_PROGRESS);
        for (EstimateProductDto epDto : estimateDto.getEstimateProducts()) {
            EstimateProduct ep = EstimateProductDto.toEntity(epDto);
            ep.setProduct(productRepository.getById(epDto.getProduct().getProductId()));
            for (EstimateProductGroupDto epgDto : epDto.getEstimateProductGroups()) {
                EstimateProductGroup epg = EstimateProductGroupDto.toEntity(epgDto);

                // List<ProductOption> productOptions = productOptionRepository.findAllById(epgDto.getEstimateProductOptions());
                // for (ProductOption po : productOptions) {
                //     EstimateProductOption epo = new EstimateProductOption();
                //     epo.setProductOption(po);
                //     epg.addEstimateProductOption(epo);
                // }
                for (Long id : epgDto.getEstimateProductOptions()) {
                    EstimateProductOption epo = new EstimateProductOption();
                    epo.setProductOption(productOptionRepository.getById(id));
                    epg.addEstimateProductOption(epo);
                }

                ep.addEstimateProductGroup(epg);
            }

            for (EstimatePrintDto eprDto : epDto.getEstimatePrints()) {
                EstimatePrint epr = EstimatePrintDto.toEntity(eprDto);
                epr.setPrintPosition(printPositionRepository.getById(eprDto.getPrintPosition().getPrintPositionId()));
                epr.setPrintType(printTypeRepository.getById(eprDto.getPrintType().getPrintTypeId()));
                epr.setPrintSize(printSizeRepository.getById(eprDto.getPrintSize().getPrintSizeId()));

                List<Color> colors = colorRepository.findAllById(eprDto.getEstimatePrintColors().stream().map(e -> e.getColor().getColorId()).collect(Collectors.toList()));
                for (Color c : colors) {
                    EstimatePrintColor epc = new EstimatePrintColor();
                    epc.setColor(c);
                    epr.addEstimatePrintColor(epc);
                }

                for (EstimatePrintPictureDto eppDto : eprDto.getEstimatePrintPictures()) {
                    try {
                        EstimatePrintPicture epp = new EstimatePrintPicture();
                        EstimateDto.Req.PictureDto pictureDto = eppDto.getPicture();
                        MultipartFile file = fileMap.get(pictureDto.getFilename());
                        String uuid = UUID.randomUUID().toString();         // S3 객체키로 사용되기 위한 UUID
    
                        ObjectMetadata objectMetadata = new ObjectMetadata();
                        objectMetadata.setContentType(file.getContentType());   // 파일의 Content-Type 반영
        
                        PutObjectRequest request = new PutObjectRequest(bucket, "images/" + uuid, file.getInputStream(), objectMetadata);
                        amazonS3Client.putObject(request);  // S3 업로드
    
                        // 업로드된 파일 정보를 테이블에 저장
                        epp.setPicture(Picture.builder().name(pictureDto.getName()).filename(uuid).path("https://d31lhh7fv7wl78.cloudfront.net/images/").filesize(Long.valueOf(file.getSize()).intValue()).build());

                        epr.addEstimatePrintPicture(epp);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        return false;
                    }
                }

                ep.addEstimatePrint(epr);
            }

            estimate.addEstimateProduct(ep);
        }

        estimateRepository.save(estimate);
        response.setData(estimate.getEstimateId());

        return true;
    }

    public boolean updateEstimate(Long memberId, Long estimateId, List<MultipartFile> files, EstimateDto.Req estimateDto, Response response) {
        // 파일이름(클라이언트에서 uuid로 넘겨준)을 id값으로 한 Map 객체 생성
        Map<String, MultipartFile> fileMap = null;
        if (files != null) fileMap = files.stream().collect(Collectors.toMap(MultipartFile::getOriginalFilename, item -> item));
        else fileMap = new HashMap<String, MultipartFile>();
        
        Estimate estimate = estimateRepository.getById(estimateId);
        EstimateDto.Req.toEntity(estimateDto, estimate);
        if (!estimateDto.isTempYn())
            estimate.setStatus(EstimateStatus.ESTIMATE_IN_PROGRESS);
        
        if (memberId != null) estimate.setMember(memberRepository.getById(memberId));
        estimate.setStatus(EstimateStatus.ESTIMATE_IN_PROGRESS);
        for (EstimateProductDto epDto : estimateDto.getEstimateProducts()) {
            EstimateProduct ep;
            if (epDto.getEstimateProductId() != null) ep = estimateProductRepository.getByEstimateProductIdAndEstimateMemberMemberId(epDto.getEstimateProductId(), memberId);
            else ep = new EstimateProduct();

            EstimateProductDto.toEntity(epDto, ep);
            
            ep.setProduct(productRepository.getById(epDto.getProduct().getProductId()));
            ep.getEstimateProductGroups().clear();
            for (EstimateProductGroupDto epgDto : epDto.getEstimateProductGroups()) {
                EstimateProductGroup epg = EstimateProductGroupDto.toEntity(epgDto);

                epg.getEstimateProductOptions().clear();
                for (Long id : epgDto.getEstimateProductOptions()) {
                    EstimateProductOption epo = new EstimateProductOption();
                    epo.setProductOption(productOptionRepository.getById(id));
                    epg.addEstimateProductOption(epo);
                }

                ep.addEstimateProductGroup(epg);
            }

            ep.getEstimatePrints().clear();
            for (EstimatePrintDto eprDto : epDto.getEstimatePrints()) {
                EstimatePrint epr = EstimatePrintDto.toEntity(eprDto);
                
                epr.setPrintPosition(printPositionRepository.getById(eprDto.getPrintPosition().getPrintPositionId()));
                epr.setPrintType(printTypeRepository.getById(eprDto.getPrintType().getPrintTypeId()));
                epr.setPrintSize(printSizeRepository.getById(eprDto.getPrintSize().getPrintSizeId()));

                List<Color> colors = colorRepository.findAllById(eprDto.getEstimatePrintColors().stream().map(e -> e.getColor().getColorId()).collect(Collectors.toList()));
                epr.getEstimatePrintColors().clear();
                for (Color c : colors) {
                    EstimatePrintColor epc = new EstimatePrintColor();
                    epc.setColor(c);
                    epr.addEstimatePrintColor(epc);
                }

                for (EstimatePrintPictureDto eppDto : eprDto.getEstimatePrintPictures()) {
                    try {
                        EstimatePrintPicture epp = new EstimatePrintPicture();
                        if (eppDto.getPicture().getPictureId() != null)
                            epp.setPicture(pictureRepository.getById(eppDto.getPicture().getPictureId()));
                        else {
                            EstimateDto.Req.PictureDto pictureDto = eppDto.getPicture();
                            MultipartFile file = fileMap.get(pictureDto.getFilename());
                            String uuid = UUID.randomUUID().toString();         // S3 객체키로 사용되기 위한 UUID
        
                            ObjectMetadata objectMetadata = new ObjectMetadata();
                            objectMetadata.setContentType(file.getContentType());   // 파일의 Content-Type 반영
            
                            PutObjectRequest request = new PutObjectRequest(bucket, "images/" + uuid, file.getInputStream(), objectMetadata);
                            amazonS3Client.putObject(request);  // S3 업로드
        
                            // 업로드된 파일 정보를 테이블에 저장
                            Picture picture = Picture.builder().name(pictureDto.getName()).filename(uuid).path("https://d31lhh7fv7wl78.cloudfront.net/images/").filesize(Long.valueOf(file.getSize()).intValue()).build();
                            pictureRepository.save(picture);
                            epp.setPicture(picture);
                        }

                        epr.addEstimatePrintPicture(epp);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        return false;
                    }
                }

                ep.addEstimatePrint(epr);
            }

            estimate.addEstimateProduct(ep);
        }

        estimateRepository.save(estimate);
        response.setData(estimate.getEstimateId());

        return true;
    }

    public EstimateDto.Detail.Res getEstimateDetail(Long estimateId) {
        return modelMapper.map(estimateRepository.findById(estimateId).get(), EstimateDto.Detail.Res.class);
    }
}