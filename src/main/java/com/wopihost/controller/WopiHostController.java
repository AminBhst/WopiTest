package com.wopihost.controller;

import com.wopihost.entity.FileInfo;
import com.wopihost.service.WopiHostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * WOPI HOST mian controller
 *
 * @author ethendev
 * @date 2017/4/15
 */
@RestController
@RequestMapping(value = "/wopi")
public class WopiHostController {

    private WopiHostService wopiHostService;

    @Autowired
    public WopiHostController(WopiHostService wopiHostService) {
        this.wopiHostService = wopiHostService;
    }

    /**
     * search a file from the host, return a file’s binary contents
     */
    @GetMapping("/files/{name}/contents")
    public void getFile(@PathVariable String name, HttpServletResponse response) {
        wopiHostService.getFile(name, response);
    }

    /**
     * The postFile operation updates a file’s binary contents.
     */
    @PostMapping("/files/{name}/contents")
    public void postFile(@PathVariable(name = "name") String name, @RequestBody byte[] content,
                         HttpServletRequest request) {
        wopiHostService.postFile(name, content, request);
    }

    /**
     * returns information about a file, a user’s permissions on that file,
     * and general information about the capabilities that the WOPI host has on the file.
     */
    @GetMapping("/files/{name}")
    public ResponseEntity<FileInfo> checkFileInfo(@PathVariable(name = "name") String name, HttpServletRequest request) throws Exception {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                System.out.println("Header: " + request.getHeader(headerNames.nextElement()));
            }
        }

        if (request.getCookies() != null) {
            for (final Cookie cookie : request.getCookies()) {
                System.out.println("COok: ");
                System.out.println(cookie.getName());
            }
        }



        Enumeration<String> attrs = request.getAttributeNames();
        if (attrs!= null) {
            while (attrs.hasMoreElements()) {
                System.out.println("attr: " + request.getAttribute(attrs.nextElement()));
            }
        }


        return wopiHostService.getFileInfo(name);
    }

    /**
     * Handling lock related operations
     */
    @PostMapping("/files/{name}")
    public ResponseEntity handleLock(@PathVariable(name = "name") String name, HttpServletRequest request) {
        return wopiHostService.handleLock(name, request);
    }

}