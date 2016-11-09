# templatemail

A simple template-based email library for Java 8.

Applications often need to send transactional emails, such as welcomes, notifications, and password resets. This simple library implements an `TemplateEmailer` class which abstracts out the email service, allowing your code to easily send templates without worrying about the underlying implementation.

Currently supported services:
  * [Amazon SES](https://aws.amazon.com/ses/)
  * [Sailthru](https://getstarted.sailthru.com/)

(Full disclosure: The library author is the former CTO of Sailthru, so these are the services I personally like and use. But  happy to accept contributions for other email services.)

For Amazon SES, email templates are assumed to be stored locally, using [Mustache](https://mustache.github.io/) templates. Sailthru hosts email templates on the service, so templates do not need to be maintained locally.

## Maven Installation

Add the following dependency to your POM file:

```
<dependency>
    <groupId>io.stardog</groupId>
    <artifactId>templatemail</artifactId>
    <version>0.1.0</version>
</dependency>
```

Will publish this on Maven Central soon, but for the moment you need to add my private Maven repo to your `<repositories>` group:

```
<repository>
    <id>stardog-maven</id>
    <name>stardog-maven</name>
    <url>http://maven.stardog.io</url>
</repository>
```

## Example Usage

```java
AmazonSimpleEmailServiceClient sesClient = new AmazonSimpleEmailServiceClient();
TemplateEmailer emailer = new AmazonSesEmailer(sesClient);

// add any number of global variables. These vars will be passed to every template automatically
emailer.putGlobalVar("MY_GLOBAL_VAR", "MY_GLOBAL_VALUE");

// add any number of templates
emailer.addTemplate(EmailTemplate.builder()
        .templateName("welcome")
        .fromEmail("info@example.com")
        .fromName("Example Name")
        .subject("Welcome, {{name}}!")
        .contentHtml("<p>Welcome to our service, {{name}}. Var: {{myvar}}</p>")
        .build());
        
// send a template
Map<String,Object> vars = new HashMap<>();
vars.put("myvar", "var value");
emailer.sendTemplate("welcome", "useremail@example.com", "User Name", vars);
```
