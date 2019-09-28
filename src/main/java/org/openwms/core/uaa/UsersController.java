/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.core.uaa;

import org.ameba.http.Response;
import org.ameba.mapping.BeanMapper;
import org.openwms.core.uaa.impl.User;
import org.openwms.core.uaa.impl.UserPassword;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An UsersController represents a RESTful access to <tt>User</tt>s. It is transactional by the means it is the outer application service
 * facade that returns validated and completed <tt>User</tt> objects to its clients.
 *
 * @author Heiko Scherrer
 */
@RestController
public class UsersController {

    private final UserService service;
    private final BeanMapper m;

    public UsersController(UserService service, BeanMapper m) {
        this.service = service;
        this.m = m;
    }

    /**
     * This method returns all existing <tt>User</tt>s. <p> <p> <table> <tr> <td>URI</td> <td>/users</td> </tr> <tr> <td>Verb</td>
     * <td>GET</td> </tr> <tr> <td>Auth</td> <td>YES</td> </tr> <tr> <td>Header</td> <td></td> </tr> </table> </p> <p> The response stores
     * <tt>User</tt> instances JSON encoded. It contains a collection of <tt>User</tt> objects. </p>
     *
     * @return JSON response
     */
    @GetMapping(UAAConstants.API_USERS)
    public List<UserVO> findAllUsers() {
        List<UserVO> users = m.map(new ArrayList<>(service.findAll()), UserVO.class);
        return users;
    }

    /**
     * Takes a newly created <tt>User</tt> instance and persists it. <p> <p> <table> <tr> <td>URI</td> <td>/users</td> </tr> <tr>
     * <td>Verb</td> <td>POST</td> </tr> <tr> <td>Auth</td> <td>YES</td> </tr> <tr> <td>Header</td> <td></td> </tr> </table> </p> <p>
     * Request Body <p>
     * <pre>
     *   {
     *     "username" : "testuser"
     *   }
     * </pre>
     * <p> Parameters: <ul> <li>username (String):</li> The unique username. </ul> </p> <p> Response Body <p>
     * <pre>
     *   {
     *     "id" : 4711,
     *     "username" : "testuser",
     *     "token" : "876sjh36ejwhd",
     *     "version" : 1
     *   }
     * </pre>
     * <p> <ul> <li>id (Integer (32bit)):</li> The internal unique technical key for the stored instance. <li>username (String):</li> The
     * unique username. <li>token (String):</li> A generated token that is used to authenticate each request. <li>version (Integer
     * (32bit)):</li> A version number used internally for optimistic locking. </ul> </p>
     *
     * @param user The user to create
     * @return a responseVO
     */
    @PostMapping(UAAConstants.API_USERS)
    public UserVO create(@RequestBody @Valid @NotNull UserVO user, HttpServletRequest req, HttpServletResponse resp) {
        User createdUser = service.create(m.map(user, User.class));
        resp.addHeader(HttpHeaders.LOCATION, getLocationForCreatedResource(req, createdUser.getPk().toString()));
        return m.map(createdUser, UserVO.class);
    }

    private String getLocationForCreatedResource(HttpServletRequest req, String objId) {
        StringBuffer url = req.getRequestURL();
        UriTemplate template = new UriTemplate(url.append("/{objId}/").toString());
        return template.expand(objId).toASCIIString();
    }

    /**
     * FIXME [scherrer] Comment this
     *
     * @param user
     * @return a responseVO
     */
    @PutMapping(UAAConstants.API_USERS)
    public UserVO save(@RequestBody @Valid UserVO user) {
        User eo = m.map(user, User.class);
        UserVO saved = m.map(service.save(eo), UserVO.class);
        return saved;
    }

    /**
     * FIXME [scherrer] Comment this
     *
     * @param image The image to save
     * @param id The users persisted id
     */
    @PatchMapping(UAAConstants.API_USERS + "/{id}")
    public void saveImage(@RequestBody @NotNull byte[] image, @PathVariable("id") @NotNull Long id) {
        service.uploadImageFile(id, image);
    }

    /**
     * FIXME [scherrer] Comment this
     *
     * @param names
     * @return a responseVO
     * @throws Exception
     */
    @DeleteMapping(UAAConstants.API_USERS + "/{name}")
    public ResponseEntity<Response> remove(@PathVariable("name") @NotNull String... names) {
        /*
        Response result = new Response();
        HttpStatus resultStatus = HttpStatus.OK;
        for (String name : names) {
            if (name == null || name.isEmpty()) {
                continue;
            }
            try {
                service.remove(name);
                result.add(new Response.ItemBuilder().wStatus(HttpStatus.OK).wParams(name).build());
            } catch (Exception sre) {
                resultStatus = HttpStatus.NOT_FOUND;
                Response.ResponseItem item = new Response.ItemBuilder().wMessage(sre.getMessage())
                        .wStatus(HttpStatus.INTERNAL_SERVER_ERROR).wParams(name).build();
                if (NotFoundException.class.equals(sre.getClass())) {
                    item.httpStatus = HttpStatus.NOT_FOUND;
                }
                result.add(item);
            }
        }
        return new ResponseEntity<Response>(result, resultStatus);
        */
        return null;
    }

    private User findByUsername(String pUsername) {
        Collection<User> users = service.findAll();
        for (User user : users) {
            if (user.getUsername().equals(pUsername)) {
                return user;
            }
        }
        return null;
    }

    // @RequestMapping(method = RequestMethod.PUT, produces =
    // MediaType.APPLICATION_JSON_VALUE, consumes =
    // MediaType.APPLICATION_JSON_VALUE, headers =
    // "Content-Type=application/json")
    // @ResponseBody
    public void changeUserPassword(@RequestBody UserPassword userPassword) {
        service.changeUserPassword(userPassword);
    }
}
