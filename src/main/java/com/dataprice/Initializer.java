package com.dataprice;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.dataprice.model.entity.Role;
import com.dataprice.model.entity.User;
import com.dataprice.service.security.RegisterUserService;
import com.dataprice.service.security.UserServiceImpl;


@Component
public class Initializer {
   
	@Autowired
	private RegisterUserService registerUserService;
	 
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	private String password = "Harbinger1945";
    private String adminUsername = "admin";
      
      @PostConstruct
      private void init()
      {
          buildAdmin();     
      }
      
      
      private void buildAdmin()
      {       
          //here I try to retrieve the Admin from my persistence layer
         User admin = userServiceImpl.getUserByUsername(adminUsername);
          try
          {
              //If the application is started for the first time (e.g., the admin is not in the DB)
              if(admin==null)
              {   
            	  registerUserService.save(adminUsername,password,Role.Admin);               
              }
              //if the application has previously been started (e.g., the admin is already present in the DB)
              else
              {
            	 //Do nothing!!  
              }                   
             
          }
          catch (Exception e)
          {
              e.printStackTrace();
              System.out.println("Errors occurred during initialization. System verification is required.");
          }
      }
      
      
}
