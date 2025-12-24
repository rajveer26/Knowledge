package com.knowledge.microservice.auth;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service("authAbstract")
@Data
public class AuthAbstract
{

	private String bearerToken = null;

}
