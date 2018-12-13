/** \file
 * 
 * Oct 8, 2018
 *
 * Copyright Ian Kaplan 2018
 *
 * @author Ian Kaplan, www.bearcave.com, iank@bearcave.com
 */
package cognitodemo.freemarker.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.google.common.base.Strings;

import cognitodemo.freemarker.services.LoginInfo;
import cognitodemo.freemarker.services.UserInfo;

/**
 * <h4>
 * IndexController
 * </h4>
 * <p>
 * Support for the index.html page and the login form.
 * </p>
 * <p>
 * Nov 29, 2018
 * </p>
 * 
 * @author Ian Kaplan, iank@bearcave.com
 */
@Controller
public class IndexController extends AuthenticationBase {

    /**
     * <p>
     * Main index.html page. This page has the login dialog, which is processed (via a post) by the
     * LoginController
     * </p>
     * 
     * @param model
     * @return
     */
    @GetMapping("/")
    public ModelAndView index(ModelMap model, HttpServletRequest request ) {
        String nextPage = "index";
        UserInfo info = (UserInfo)request.getSession().getAttribute(USER_SESSION_ATTR);
        if (info != null) {
            // the user is already logged in, so redirect to the application page.
            nextPage = "application";
            model.addAttribute(USER_SESSION_ATTR, info);
        }
        return new ModelAndView(nextPage, model); 
    } // index
    
    
    /**
     * <p>
     * Handle the input (user, password) from the login form.
     * </p> 
     * @param user
     * @param password
     * @param redirect
     * @return
     */
    @PostMapping("/login_form")
    public String login( @RequestParam("user_name") String user,
                         @RequestParam("password") String password,
                         RedirectAttributes redirect,
                         HttpServletRequest request) {
        boolean hasErrors = false;
        String userNameArg = null;
        String passwordArg = null;
        
        // By default, if there's an error, we go back to the index page (with the login form).
        String nextPage = "redirect:/";
        if (Strings.isNullOrEmpty(user)) {
            redirect.addFlashAttribute("user_name_error", "Please supply a user name");
            hasErrors = true;
        } else {
            userNameArg = user.trim();
            redirect.addFlashAttribute("user_name_val", userNameArg);
        }
        if (! hasErrors) {
            if (Strings.isNullOrEmpty(password)) {
                redirect.addFlashAttribute("password_error", "Please enter your password");
                hasErrors = true;
            } else {
                passwordArg = password.trim();
            }
        }
        if (! hasErrors) {
            /* The user name and password have been entered. Now try the login process.  There are three outcomes:
             * 1. The login succeeds and control passes to the application page.
             * 2. A password change is required (e.g., for a new user) and control goes to the change password page.
             * 3. The login fails and we go back to the index page with the login form.
            */
            try {
                LoginInfo loginInfo = authService.userLogin(userNameArg, passwordArg);
                if (loginInfo != null) {
                    if (loginInfo.getNewPasswordRequired()) {
                        nextPage = "redirect:change_password";
                        redirect.addFlashAttribute("user_name_val", userNameArg);
                        redirect.addFlashAttribute("change_type", "change_password");
                    } else {
                        nextPage = "redirect:application";
                        UserInfo userInfo = new UserInfo(loginInfo.getUserName(), loginInfo.getEmailAddr(), loginInfo.getLocation());
                        // Set the session attribute
                        request.getSession().setAttribute(USER_SESSION_ATTR, userInfo);
                    }
                }
            } catch (NotAuthorizedException e) {
                redirect.addFlashAttribute("login_error", "Incorrect username or password");
            } catch (Exception e) {
                redirect.addFlashAttribute("login_error", "Error in login: " + e.getClass().getName() + " " + e.getLocalizedMessage());
            }
        }
        return nextPage;
    } // login
    
}
