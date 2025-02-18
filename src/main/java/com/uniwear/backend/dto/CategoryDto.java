package com.uniwear.backend.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CategoryDto {
    public static class Default {
        @Getter
        @Setter
        public static class Res {
            private Long categoryId;
            private String name;
            private String value;
            private int order;
            private Long parentCategoryId;
            private Parent parentCategory;
            private List<Child> categories;
        }
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Parent {
            private Long categoryId;
            private String name;
            private String value;
            private int order;
            private List<Child> categories;
        }
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Child {
            private Long categoryId;
            private String name;
            private String value;
            private int order;
            private Long parentCategoryId;
        }
    }
    public static class Root {
        @Getter
        @Setter
        public static class Res {
            private Long categoryId;
            private String name;
            private String value;
            private int order;
            private Long parentCategoryId;
        }
    }
    public static class Menu {
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Res {
            private Long categoryId;
            private String name;
            private String value;
            private int order;
            private Long parentCategoryId;
            private List<Menu.Res> categories;
        }
    }
}