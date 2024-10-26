//package com.spring.ai.es.entity;
//
//import lombok.Data;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
///**
// * @author ll
// * @since 2024-10-24 22:55
// */
//@Data
////@Document(indexName = "kibana_sample_data_ecommerce")
//public class Ecommerce {
//    private String category;
//
//    private String currency;
//
////    @Field(name = "customer_birth_date")
//    private Date customerBirthDate;
//
//    @Field(name = "customer_first_name")
//    private String customerFirstName;
//
//    @Field(name = "customer_full_name")
//    private String customerFullName;
//
//    @Field(name = "customer_gender")
//    private String customerGender;
//
//    @Field(name = "customer_id")
//    private String customerId;
//
//    @Field(name = "customer_last_name")
//    private String customerLastName;
//
//    @Field(name = "customer_phone")
//    private String customerPhone;
//
//    @Field(name = "day_of_week")
//    private String dayOfWeek;
//
//    @Field(name = "day_of_week_i")
//    private Integer dayOfWeekI;
//
//    private String email;
//
//    private Event event;
//
//    @Field(name = "geoip")
//    private GeoIp geoIp;
//
//    private String manufacturer;
//
//    @Field(name = "order_date")
//    private Date orderDate;
//
//    @Field(name = "order_id")
//    private String orderId;
//
//    private Products products;
//
//    private String sku;
//
//    @Field(name = "taxful_total_price")
//    private BigDecimal taxfulTotalPrice;
//
//    @Field(name = "taxless_total_price")
//    private BigDecimal taxlessTotalPrice;
//
//    @Field(name = "total_quantity")
//    private Integer totalQuantity;
//
//    @Field(name = "total_unique_products")
//    private Integer totalUniqueProducts;
//
//    private String type;
//
//    private String user;
//
//    @Data
//    public static class Event {
//        private String dataset;
//    }
//
//    @Data
//    public static class GeoIp {
//        @Field(name = "city_name")
//        private String cityName;
//
//        @Field(name = "continent_name")
//        private String continentName;
//
//        @Field(name = "country_iso_code")
//        private String countryIsoCode;
//
//        private String location;
//
//        @Field(name = "region_name")
//        private String regionName;
//    }
//
//    @Data
//    public static class Products {
//        @Field(name = "_id")
//        private String id;
//
//        @Field(name = "base_price")
//        private String basePrice;
//
//        @Field(name = "base_unit_price")
//        private String baseUnitPrice;
//
//        private String category;
//
//        @Field(name = "created_on")
//        private Date createdOn;
//
//        @Field(name = "discount_amount")
//        private BigDecimal discountAmount;
//
//        @Field(name = "discount_percentage")
//        private BigDecimal discountPercentage;
//
//        private String manufacturer;
//
//        @Field(name = "min_price")
//        private BigDecimal minPrice;
//
//        private BigDecimal price;
//
//        @Field(name = "product_id")
//        private Long productId;
//
//        @Field(name = "product_name", analyzer = "english")
//        private String productName;
//
//        private Integer quantity;
//
//        private String sku;
//
//        @Field(name = "tax_amount")
//        private BigDecimal taxAmount;
//
//        @Field(name = "taxful_price")
//        private BigDecimal taxfulPrice;
//
//        @Field(name = "taxless_price")
//        private BigDecimal taxlessPrice;
//
//        @Field(name = "unit_discount_amount")
//        private BigDecimal unitDiscountAmount;
//    }
//}
//
