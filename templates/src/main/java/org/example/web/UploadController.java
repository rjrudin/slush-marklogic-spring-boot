package org.example.web;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklogic.contentpump.bean.MlcpBean;
import com.marklogic.spring.http.RestConfig;

@Controller
public class UploadController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestConfig restConfig;

    @Autowired
    private CredentialsProvider credentialsProvider;

    /**
     * Using a REST-API-like URL so that the CORS request matcher matches it.
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/v1/upload", method = RequestMethod.PUT)
    public ResponseEntity<?> upload(HttpServletRequest request) throws Exception {
        CommonsMultipartResolver r = new CommonsMultipartResolver();
        MultipartHttpServletRequest req = r.resolveMultipart(request);
        MultipartFile mfile = req.getFile("file");

        String filename = mfile.getOriginalFilename();
        String json = req.getParameter("data");

        String tempFilename = filename;
        String extension = null;
        int pos = filename.lastIndexOf('.');
        if (pos > -1) {
            tempFilename = filename.substring(0, pos);
            extension = filename.substring(pos);
        }
        try {
            Path path = Files.createTempFile(tempFilename, extension);
            File file = path.toFile();
            logger.info("Copying uploaded file to temp file: " + file.getAbsolutePath());
            FileCopyUtils.copy(mfile.getBytes(), file);

            filename = file.getName();

            // Use user inputs to construct an MlcpBean, then set the properties we know the user can't (and shouldn't)
            // be able to set
            MlcpBean bean = new ObjectMapper().readerFor(MlcpBean.class).readValue(json);
            bean.setHost(restConfig.getHost());
            bean.setPort(restConfig.getRestPort());

            // Assume that the HTTP credentials will work for mlcp
            Credentials creds = credentialsProvider.getCredentials(AuthScope.ANY);
            bean.setUsername(creds.getUserPrincipal().getName());
            bean.setPassword(creds.getPassword());

            // Path to file
            bean.setInput_file_path(file.getAbsolutePath());
            if (bean.getInput_file_type() == null
                    && (".csv".equals(extension) || ".xls".equals(extension) || ".xlsx".equals(extension))) {
                bean.setInput_file_type("delimited_text");
            }

            bean.run();
        } catch (Exception ie) {
            throw new RuntimeException(ie);
        }

        return null;
    }
}
