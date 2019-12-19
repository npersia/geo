/**
 * 
 */
package com.telecom.geographicaddressmanagement.domain.model;

import lombok.Data;

/**
 * @author PABLODANIELDELBUONO
 *
 */
@Data
public class DataArea {

	private Long id;
	private String type;
	private String name;
	private String fathers;
	private String characteristics;
	private String identification;
}
