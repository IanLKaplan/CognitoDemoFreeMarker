# CognitoDemoFreeMarker

This repository contains a version of the Cognito demonstration application (see https://github.com/IanLKaplan/CognitoDemo) which uses FreeMarker as the webpage template engine.  This application uses Spring Boot.

A similar demonstration application was created using the Thymeleaf template engine (see https://github.com/IanLKaplan/CognitoDemoThymeleaf)

In addition to providing examples of FreeMarker template pages, this application shows how to configure Spring Boot to allow images (e.g., jpg, png, etc...) to be loaded in a FreeMarker page. See the AppConfig class in the cognitodemo.freemarker.config package.

The original [Cognito demo application](https://github.com/IanLKaplan/CognitoDemo) was developed to explore the Cognito authentication service provided by Amazon Web Services. The web pages for this application use the Java Server Pages (JSP) template engine.

The JSP template engine dates to the early days of Java. While JSP is still in use, Oracle does not seem to be maintaining or extending JSP. Nor does the open source Java community seem to be doing anything with JSP.

JSP has other issues. It does not handle including web page fragements, which provides an important way to manage web page complexity and avoid constantly having to copy web page content for each new page.

As I note in the [Cognito demo with Thymeleaf repository](https://github.com/IanLKaplan/CognitoDemoThymeleaf), Thymeleaf is popular in the Spring community. However, Thymeleaf is maintained by a small group of people and there is a possibility that it would become an orphan project in the future.

FreeMarker is an [Apache project](https://freemarker.apache.org/) and has a large user community. This community includes a number of commerical content management systems (CMS) which use FreeMarker for web page creation. The large FreeMarker community makes it less likely that FreeeMarker will become an orphan project.

FreeMarker is also supported by the Spring framework.

When the (current version of) Thymeleaf encounters an error, it throws a Java exception with no information on the source of the problem. This can make finding Thymeleaf errors very frustrating and time consuming. In contrast, when FreeMarker encounters a problem, it reports the line number and often the cause of the problem.

Finally, a matter of personal taste, I like the FreeMarker markup better than the Thymeleaf or JSP markup.

Which is not to say that FreeMarker is not without its quirks. I spent hours of frustrating experimentation before I could get images to load on the FreeMarker pages. Free marker also has a syntax that can take getting used to.  For example, if a FreeMarker page is passed the String variable login_error, the code below will cause a FreeMarker parsing error.

```html
<#if login_error != null && (login_error.length() > 0)>
```
FreeMarker supports operators for comparison to null and for string length. The null comparision operator is "??"  The .length() function is not allowed. Instead the built-in length operator must be used: login_error?length. The correct version is shown below:

```html
<#if login_error?? && (login_error?length > 0)>
  ```
 Each of the web pages uses FreeMarker includes for the page titles and the includes for CSS and JavaScript.
 
