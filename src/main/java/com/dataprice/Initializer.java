package com.dataprice;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dataprice.model.entity.Country;
import com.dataprice.model.entity.Retail;
import com.dataprice.model.entity.Role;
import com.dataprice.model.entity.User;
import com.dataprice.service.addcountryservice.AddCountryService;
import com.dataprice.service.addretailservice.AddRetailService;
import com.dataprice.service.security.RegisterUserService;
import com.dataprice.service.security.UserServiceImpl;


@Component
public class Initializer {
   
	@Autowired
	private RegisterUserService registerUserService;
	 
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired 
	private AddCountryService addCountryService;
	
	@Autowired 
	private AddRetailService addRetailService;
		
	private String password = "Harbinger1945";
    private String adminUsername = "12345";
      
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
              
              //Always add this retails when started the application.
            
          	Country country = new Country();
			country.setCountryId(1);
			country.setCountryName("México");
			country.setCurrency("Peso MXN");
			country.setNickname("MX");
			addCountryService.saveCountry(country);	
			
			Retail retail1 = new Retail();
			retail1.setRetailId(1);
			retail1.setRetailName("Mercado Libre");
			retail1.setCrawlerName("MercadoLibre");
			retail1.setCountry(country);		
			addRetailService.saveRetail(retail1);
			
			
			Retail retail2 = new Retail();
			retail2.setRetailId(2);
			retail2.setRetailName("Linio");
			retail2.setCrawlerName("Linio");
			retail2.setCountry(country);		
			addRetailService.saveRetail(retail2);
			
			
			Retail retail3 = new Retail();
			retail3.setRetailId(3);
			retail3.setRetailName("Chedraui");
			retail3.setCrawlerName("Chedraui");
			retail3.setCountry(country);		
			addRetailService.saveRetail(retail3);
			
			
             
          }
          catch (Exception e)
          {
              e.printStackTrace();
              System.out.println("Errors occurred during initialization. System verification is required.");
          }
      }
      
      
}
