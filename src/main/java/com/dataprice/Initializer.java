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
		
	private String password = "12345";
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
              
              //Always add this retails when started the application.
            
          	Country mexico = new Country();
          	mexico.setCountryId(1);
          	mexico.setCountryName("México");
			mexico.setCurrency("Peso MXN");
			mexico.setNickname("MX");
			addCountryService.saveCountry(mexico);	
			
			
			Country usa = new Country();
			usa.setCountryId(2);
			usa.setCountryName("United States");
			usa.setCurrency("USD");
			usa.setNickname("US");
			addCountryService.saveCountry(usa);
			
			
			Retail retail1 = new Retail();
			retail1.setRetailId(1);
			retail1.setRetailName("Amazon");
			retail1.setCrawlerName("Amazon");
			retail1.setCountry(mexico);		
			addRetailService.saveRetail(retail1);
			
			
			Retail retail2 = new Retail();
			retail2.setRetailId(2);
			retail2.setRetailName("Arome");
			retail2.setCrawlerName("Arome");
			retail2.setCountry(mexico);		
			addRetailService.saveRetail(retail2);
			
			
			Retail retail3 = new Retail();
			retail3.setRetailId(3);
			retail3.setRetailName("Chedraui");
			retail3.setCrawlerName("Chedraui");
			retail3.setCountry(mexico);		
			addRetailService.saveRetail(retail3);
			
			
			Retail retail4 = new Retail();
			retail4.setRetailId(4);
			retail4.setRetailName("Laeuropea");
			retail4.setCrawlerName("Laeuropea");
			retail4.setCountry(mexico);		
			addRetailService.saveRetail(retail4);
			
			Retail retail5 = new Retail();
			retail5.setRetailId(5);
			retail5.setRetailName("Linio");
			retail5.setCrawlerName("Linio");
			retail5.setCountry(mexico);		
			addRetailService.saveRetail(retail5);
			
			Retail retail6 = new Retail();
			retail6.setRetailId(6);
			retail6.setRetailName("Liverpool");
			retail6.setCrawlerName("Liverpool");
			retail6.setCountry(mexico);		
			addRetailService.saveRetail(retail6);
			
			Retail retail7 = new Retail();
			retail7.setRetailId(7);
			retail7.setRetailName("MercadoLibre");
			retail7.setCrawlerName("MercadoLibre");
			retail7.setCountry(mexico);		
			addRetailService.saveRetail(retail7);
			
			Retail retail8 = new Retail();
			retail8.setRetailId(8);
			retail8.setRetailName("NutritionDepot");
			retail8.setCrawlerName("NutritionDepot");
			retail8.setCountry(mexico);		
			addRetailService.saveRetail(retail8);
			
			Retail retail9 = new Retail();
			retail9.setRetailId(9);
			retail9.setRetailName("Osom");
			retail9.setCrawlerName("Osom");
			retail9.setCountry(mexico);		
			addRetailService.saveRetail(retail9);
			
			Retail retail10 = new Retail();
			retail10.setRetailId(10);
			retail10.setRetailName("PerfumesMexico");
			retail10.setCrawlerName("PerfumesMexico");
			retail10.setCountry(mexico);		
			addRetailService.saveRetail(retail10);
			
			Retail retail11 = new Retail();
			retail11.setRetailId(11);
			retail11.setRetailName("PerfumesOnline");
			retail11.setCrawlerName("PerfumesOnline");
			retail11.setCountry(mexico);		
			addRetailService.saveRetail(retail11);
			
			
			Retail retail12 = new Retail();
			retail12.setRetailId(12);
			retail12.setRetailName("Prissa");
			retail12.setCrawlerName("Prissa");
			retail12.setCountry(mexico);		
			addRetailService.saveRetail(retail12);
			
			
			
			Retail retail13 = new Retail();
			retail13.setRetailId(13);
			retail13.setRetailName("Sanborns");
			retail13.setCrawlerName("Sanborns");
			retail13.setCountry(mexico);		
			addRetailService.saveRetail(retail13);
			
			
			Retail retail14 = new Retail();
			retail14.setRetailId(14);
			retail14.setRetailName("Soriana");
			retail14.setCrawlerName("Soriana");
			retail14.setCountry(mexico);		
			addRetailService.saveRetail(retail14);
			
			
			Retail retail15 = new Retail();
			retail15.setRetailId(15);
			retail15.setRetailName("SuperWalmart");
			retail15.setCrawlerName("SuperWalmart");
			retail15.setCountry(mexico);		
			addRetailService.saveRetail(retail15);
			
			
			Retail retail16 = new Retail();
			retail16.setRetailId(16);
			retail16.setRetailName("SuplementosFitness");
			retail16.setCrawlerName("SuplementosFitness");
			retail16.setCountry(mexico);		
			addRetailService.saveRetail(retail16);
			
			
			
			Retail retail17 = new Retail();
			retail17.setRetailId(17);
			retail17.setRetailName("Walmart");
			retail17.setCrawlerName("Walmart");
			retail17.setCountry(mexico);		
			addRetailService.saveRetail(retail17);
          }
          catch (Exception e)
          {
              e.printStackTrace();
              System.out.println("Errors occurred during initialization. System verification is required.");
          }
      }
      
      
}
