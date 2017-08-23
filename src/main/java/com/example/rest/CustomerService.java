/* Copyright © 2015 Oracle and/or its affiliates. All rights reserved. */
package com.example.rest;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers")
public class CustomerService {

  private final CopyOnWriteArrayList<Customer> cList = MockCustomerList.getInstance();

  @GET
  @Path("/all")
  //@Produces(MediaType.APPLICATION_XML)
  @Produces(MediaType.APPLICATION_JSON)
  public Customer[] getAllCustomers() {
    return cList.toArray(new Customer[0]);
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Customer getCustomer(@PathParam("id") long id){
    Optional<Customer> match
        = cList.stream()
        .filter(c -> c.getId() == id)
        .findFirst();
    if (match.isPresent()) {
      return match.get();
    } else {
      throw new NotFoundException(new JsonError("Error", "Customer " + id + " not found"));
    }
  }
  
  @POST
  @Path("/add")
  @Produces(MediaType.APPLICATION_JSON)
  public Response addCustomer(Customer customer){
    cList.add(customer);
    return Response.status(201).build();
  }
  
  @PUT
  @Path("{id}/update")
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateCustomer(Customer customer){
    int matchIdx = 0;
    Optional<Customer> match = cList.stream()
        .filter(c -> c.getId() == customer.getId())
        .findFirst();
    if (match.isPresent()) {
      matchIdx = cList.indexOf(match.get());
      cList.set(matchIdx, customer);
      return Response.status(Response.Status.OK).build();
    } else {
      return Response.status(Response.Status.NOT_FOUND).build();      
    }
  }
  
  @DELETE
  @Path("/remove/{id}")
  public Response deleteCustomer(@PathParam("id") long id){
    Predicate <Customer> customer = c -> c.getId() == id;
    if (cList.removeIf(customer)) {
        return Response.status(Response.Status.OK).build();     
    }else {
      return Response.status(Response.Status.NOT_FOUND).build();      
    }
  }
    
}
