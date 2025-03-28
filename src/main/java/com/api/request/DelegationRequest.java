package com.api.request;


import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DelegationRequest {


    Long delegue ;

    Long delegueAQui ;

    DocumentRequest document ;



   
}
