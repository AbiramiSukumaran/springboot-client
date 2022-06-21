/* Copyright 2022 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
*/

package com.example.demo;

import com.example.demo.Reservation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.RequestEntity;
import org.springframework.http.HttpMethod;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import com.google.cloud.Timestamp;
import com.google.cloud.Date;
import org.springframework.core.ParameterizedTypeReference;
import java.net.URI;

/*
 Main Class for the Spring Client Application
*/
@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

/*
 Controller class for invoking Server REST APIs 
*/

    @Controller
    class Mycontroller {
    private static final String get_reservations = "YOUR_API";
    private static final String get_reservation_by_id = "YOUR_API";
    private static final String create_reservations = "YOUR_API";
    private static final String edit_reservation = "YOUR_API";
    private static final String delete_reservation = "YOUR_API";

    RestTemplate restTemplate = new RestTemplate();

    /*
        Method to invoke API that retrieve alls Reservations 
    */
        public ResponseEntity<String> callReservationsAPI(){
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<String> result = restTemplate.exchange(get_reservations, HttpMethod.GET, entity, String.class);
            return result;
        }

        /*
            Method to invoke API that validates if that unit has an existing appointment for the day
        */
        public boolean validateId(Reservation newReservation){
            java.util.Date dt = new java.util.Date();
            java.util.Calendar c = java.util.Calendar.getInstance(); 
            c.setTime(dt); 
            dt = c.getTime();
            String idParam = newReservation.getAptId() +"_"+  Date.fromJavaUtilDate(dt);
            Map<String, String> param = new HashMap<>();
            param.put("id", idParam);
            try{
            Reservation oldReservation = restTemplate.getForObject(get_reservation_by_id, Reservation.class, param);
            
            if(oldReservation.getId().equals(idParam)){
                    //Reservation available for tomorrow
                    return true;
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }
        }

        /*
            Method to invoke API that validates if that hour has not been booked already by another unit
        */
        public boolean validateSlot(Reservation newReservation){
            java.util.Date dt = new java.util.Date();
            java.util.Calendar c = java.util.Calendar.getInstance(); 
            c.setTime(dt); 
            dt = c.getTime();
          
            String idParamDt = Date.fromJavaUtilDate(dt).toString();
            int hour = newReservation.getHourNumber();

            String idParam = get_reservations + "/" + idParamDt + "_" + hour;

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            
            ResponseEntity<String> result = restTemplate.exchange(idParam, HttpMethod.GET, entity, String.class);
            if(result.toString().equals("present")){
                return true;
            }else{
                return false;
            }
        }

        /*
            Method to invoke API that retrieves a specific reservation
        */
        @GetMapping("/showreservation")
        public String callReservationsByIdAPI(Reservation reservation){
            try{
            java.util.Date dt = new java.util.Date();
            java.util.Calendar c = java.util.Calendar.getInstance(); 
            c.setTime(dt); 
            dt = c.getTime();
            String idParam = reservation.getAptId() +"_"+  Date.fromJavaUtilDate(dt);
            Map<String, String> param = new HashMap<>();
            param.put("id", idParam);
            Reservation res = restTemplate.getForObject(get_reservation_by_id, Reservation.class, param);
            reservation.setAptId(res.getAptId());
            reservation.setHourNumber(res.getHourNumber());
            reservation.setPlayerCount(res.getPlayerCount());
            return "showMessage";
            }catch(Exception e){
                return "searchReservation";
            }
        }

        /*
            Method that is invoked on show reservations call, to return showMessage HTML page
        */
        @GetMapping("/showreservations")
        public String showForm(Reservation reservation) {
            String res = callReservationsAPI().toString();
            reservation.setAptId(res);
           return "showMessage";
        }
        
        /*
            Method that is invoked on search, to return the searchReservation HTML page 
        */
        @GetMapping("/search")
        public String searchForm(Reservation reservation) {
            return "searchReservation";
        }

        /*
            Method that is invoked on home page, to return the HomePage HTML page 
        */
        @GetMapping("/home")
        public String homeForm(Reservation reservation) {
            return "HomePage";
        }

        /*
            Method that is invoked on GET call for addreservation request, to return the addReservation HTML page 
        */
        @GetMapping("/addreservation")
        public String sendForm(Reservation reservation) {
    
            return "addReservation";
        }
    
        /*
            Method that is invoked on POST call for addreservation request 
        */
        @PostMapping("/addreservation")
        public String processForm(Reservation reservation) {
            if(validateId(reservation) || validateId(reservation)){
                return "errMessage";
            }
            ResponseEntity<String> res = restTemplate.postForEntity(create_reservations, reservation, String.class);
            reservation.setId(res.toString());
            return "showMessage";
        }

        /*
            Method that is invoked on POST call for editreservation request 
        */
        @PostMapping("/editreservation")
        public String editForm(Reservation reservation) {
            Map<String, String> param = new HashMap<>();
            java.util.Date dt = new java.util.Date();
            java.util.Calendar c = java.util.Calendar.getInstance(); 
            c.setTime(dt); 
            dt = c.getTime();
            String idParam = reservation.getAptId() +"_"+  Date.fromJavaUtilDate(dt);
            param.put("id", idParam);
            restTemplate.put(edit_reservation, reservation, param);
            return "showMessage";
        }

        /*
            Method that is invoked on POST call for deletereservation request 
        */
        @PostMapping("/deletereservation")
        public String deleteForm(Reservation reservation) {
            Map<String, String> param = new HashMap<>();
            java.util.Date dt = new java.util.Date();
            java.util.Calendar c = java.util.Calendar.getInstance(); 
            c.setTime(dt); 
            dt = c.getTime();
            String idParam = reservation.getAptId() +"_"+  Date.fromJavaUtilDate(dt);
            param.put("id", idParam);
            restTemplate.delete(delete_reservation, param);
            return "searchReservation";
        }

    }
