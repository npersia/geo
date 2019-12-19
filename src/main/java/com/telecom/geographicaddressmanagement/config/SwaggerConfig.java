/**
 * 
 */
package com.telecom.geographicaddressmanagement.config;

import com.google.common.collect.Lists;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	public static final String TAG_GEOGRAPHIC_ADDRESS = "Geographic Address";
	public static final String TAG_GEOGRAPHIC_ADDRESS_DESCRIPTION = "A geographic address is a structured textual way of describing how to find a Property in an urban area (country properties are often defined differently)";
	public static final String TAG_AREA = "Area";
	public static final String TAG_AREA_DESCRIPTION = "Area corresponds to a geographic area as a city, a locality, a district, etc";
	public static final String TAG_TYPES = "Types";

	@Bean
	public Docket productApi() {
		Docket result = null;

		result = new Docket(DocumentationType.SWAGGER_2).select()
		       // .apis((RequestHandlerSelectors.withClassAnnotation(RestController.class)))
				.apis(RequestHandlerSelectors.basePackage("com.telecom.geographicaddressmanagement.resource.v1"))
		        .paths(PathSelectors.any())
		        .build()
		        .genericModelSubstitutes(ResponseEntity.class)
		        .useDefaultResponseMessages(false)		      
		        .globalResponseMessage(RequestMethod.GET,
		                Lists.newArrayList(
		                		new ResponseMessageBuilder().code(206).message(
		                                "Partial resource returned in response (with pagination)")
		                                .build(),
		                		new ResponseMessageBuilder().code(500).message("Failed to process request").build(),
		                               // .responseModel(new ModelRef(MODEL_REFERENCE_ERROR)).build(),
		                        new ResponseMessageBuilder().code(401)
		                                .message("Unauthorized. You are not authorized to view the resource").build(),
		                        new ResponseMessageBuilder().code(403).message(
		                                "Forbidden. Accessing the resource you were trying to reach is forbidden")
		                                .build(),
		                         new ResponseMessageBuilder().code(400).message(
				                                "Bad Request. Required query parameter not found")
				                                .build()		                                    
		                		))
		        .enableUrlTemplating(true)
		        .forCodeGeneration(true)
		        .tags(new Tag(TAG_GEOGRAPHIC_ADDRESS, TAG_GEOGRAPHIC_ADDRESS_DESCRIPTION),
		        		new Tag(TAG_AREA, TAG_AREA_DESCRIPTION)).apiInfo(metaData());
		return result;

	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("Geographic Address Managment").description("The Geographic Address API provides a standardized client interface to an Address management system. It allows looking for worldwide addresses. \n It can also be used to validate geographic address data, to be sure that it corresponds to a real address. Finally, it can be used to look for an address by: searching an area as a start (city, town …), then zooming on the streets of this area, etc.")
		        .contact(new Contact("Pablo Daniel Delbuono", null, "delbuono@ar.ibm.com")).version("1.0").build();
	}

}
