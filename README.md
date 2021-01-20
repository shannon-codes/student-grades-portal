# Grades Management 

- Purpose: Organize student grades and provide CRUD functionality using Java Spring Boot.
- Backend Technologies: Java Spring Boot, Hibernate ORM and Validation, Form Binding with ThymeLeaf, Spring Security and Roles/Authority
- UI: Bootstrap, HTML, CSS
- Result: Received full marks

## Screenshots

1. Professor can add/edit/delete students and update their grades
<img src="Screenshots/s1.png" height="300px" width="500px">
<br>
2. Professor can view overall class average and average per assessment
<img src="Screenshots/s2.png" height="300px" width="500px">
<br>
3. Professor can add new students and validate using Hibernate Validation at the bean level before we store in the database.
<img src="Screenshots/s6.png" height="300px" width="500px">

4. Professor can edit the student grade details
<img src="Screenshots/s3.png" height="300px" width="500px">
<br>
5. User should have proper authority(role) to access Professor area or pages ending in /professor/**
<img src="Screenshots/s4.png" height="300px" width="500px">
<br>
6. H2 In-memory database, Notice that passwords are salted(encrypted) before stored in database.
<img src="Screenshots/s5.png" height="300px" width="500px">
<br>
7. After testing with In-memory database, we can then change to MySQL database. See screenshot of MySQL WorkBench queries and results.
<img src="Screenshots/s7.png" height="300px" width="500px">
<img src="Screenshots/s8.png" height="300px" width="500px">
