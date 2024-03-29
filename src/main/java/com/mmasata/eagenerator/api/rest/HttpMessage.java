package com.mmasata.eagenerator.api.rest;

import com.mmasata.eagenerator.api.rest.enums.HttpMessageType;
import com.mmasata.eagenerator.api.DTOPropertyWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Common-api model defining HTTP message.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpMessage {

    private DTOPropertyWrapper property;

    private HttpMessageType httpMessageType;
}
