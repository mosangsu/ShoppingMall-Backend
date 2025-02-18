package com.uniwear.backend.repository;

import java.util.List;

import com.uniwear.backend.entity.Picture;

import org.springframework.data.repository.CrudRepository;

public interface PictureRepository extends CrudRepository<Picture, Long> {
    List<Picture> findAllById(Iterable<Long> ids);
    Picture getById(Long pictureId);
}