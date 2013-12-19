package com.github.darogina.jmx;

import com.github.darogina.jmx.model.Person;
import com.github.darogina.jmx.property.JmxConnection;
import com.github.darogina.jmx.property.JmxTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {

    @Autowired(required = true)
    private Person person;

    @Autowired(required = true)
    private JmxTest jmxTest;

    @Autowired(required = true)
    private JmxConnection jmxConnection;

	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("person", person);
//        jmxTest.someTest();
        jmxConnection.connect("weblogic1", "weblogic1");
		return "hello";
	}
}