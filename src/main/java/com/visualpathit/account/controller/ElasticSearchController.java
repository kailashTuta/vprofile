package com.visualpathit.account.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.visualpathit.account.model.User;
import com.visualpathit.account.service.UserService;
import com.visualpathit.account.utils.ElasticsearchUtil;

@Controller
public class ElasticSearchController {
	@Autowired
    private UserService userService;
    
    @RequestMapping(value="/user/elasticsearch", method=RequestMethod.GET)
    public String insert(final Model model) throws IOException {
    	List<User> users = userService.getList();
    	String result ="";
    	for (User user : users) {
            Map<String, Object> document = new HashMap<>();
            document.put("name", user.getUsername());
            document.put("DOB", user.getDateOfBirth());
            document.put("fatherName", user.getFatherName());
            document.put("motherName", user.getMotherName());
            document.put("gender", user.getGender());
            document.put("nationality", user.getNationality());
            document.put("phoneNumber", user.getPhoneNumber());
            IndexResponse response = ElasticsearchUtil.elasticsearchClient().index(index -> index
                    .index("users")
                    .id(String.valueOf(user.getId()))
                    .document(document));
	        String res = response.result().toString();
	        System.out.println(res);
	        result="Users";
    	}
    	model.addAttribute(result);
        return "elasticeSearchRes";
        		
    }

    @RequestMapping(value="/rest/users/view/{id}", method=RequestMethod.GET)
    public String  view(@PathVariable("id") final String id,final Model model) throws IOException {
        GetResponse<JsonData> getResponse = ElasticsearchUtil.elasticsearchClient().get(
                request -> request.index("users").id(id), JsonData.class);
        JsonData source = getResponse.source();
        if (source == null) {
            throw new IOException("Elasticsearch returned no user document for id " + id);
        }
        System.out.println(source);

        Object document = source.to(Object.class);
        if (!(document instanceof Map<?, ?> fields)) {
            throw new IOException("Elasticsearch returned a user document that is not an object");
        }
        model.addAttribute("res", fields.get("name"));
       
        return "elasticeSearchRes";
    }
    /*@RequestMapping(value = "/get_user_list",  method = RequestMethod.GET)
    public @ResponseBody List getTagList(@RequestParam("term") String query) {
    	List<User> users = userService.getList();
    	List tagList = null;
    	for (User user : users) {
	    	GetResponse getResponse = ElasticsearchUtil.trannsportClient().prepareGet("users", "user" ,String.valueOf(user.getId())).get();
	        System.out.println(getResponse.getSource());
	        
	        tagList.add(getResponse.getSource());
    	}
        return tagList;
    }*/

    @RequestMapping(value="/rest/users/update/{id}", method=RequestMethod.GET)
    public String update(@PathVariable("id") final String id,final Model model) throws IOException {
        UpdateResponse<JsonData> updateResponse = ElasticsearchUtil.elasticsearchClient().update(
                request -> request.index("employee").id(id).doc(Map.of("gender", "male")), JsonData.class);
        System.out.println(updateResponse.result());
        model.addAttribute("res", updateResponse.result());
        return "elasticeSearchRes";
    }
    @RequestMapping(value="/rest/users/delete/{id}", method=RequestMethod.GET)
    public String delete(@PathVariable("id") final String id,final Model model) throws IOException {

        DeleteResponse deleteResponse = ElasticsearchUtil.elasticsearchClient().delete(
                request -> request.index("employee").id(id));
        System.out.println(deleteResponse.result().toString());
        model.addAttribute("res", deleteResponse.result().toString());
        return "elasticeSearchRes";
    }
    /*public void contextMapping() throws IOException{    	   	
		String json ="{"
				+ "\"mappings\":{"
				+ "\"users\":\" {"
				+ "\"properties\" : {"
				+ "\"name\" : { \"type\" : \"string\" },"
				+ " \"city\" : { \"type\" : \"string\" },"
				+ "\"name_suggest\" : {"
				+ "\"type\" :     \"completion\""
				+ "}}"
				+ "}";
		IndexResponse response = ElasticsearchUtil.trannsportClient().prepareIndex("users", "data")
								.setSource(json).execute().actionGet();
		
    }*/
}
