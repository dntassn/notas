package controllers;

import play.mvc.Before;
import play.mvc.Controller;

public class Secure extends Controller {
	
	@Before
    static void checkAuthentification() {
    	if (session.get("usuarioLogado") == null) {
    		Logins.login();
    	}
    }

}
