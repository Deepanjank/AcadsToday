drop table student;
drop table teaches cascade;
drop table prereq cascade;
drop table section cascade;
drop table course cascade;
drop table department cascade;
create table student
	(user_id		varchar(15),
	 password		varchar(7),
	 rollno		varchar(10),
	 name		varchar(20),
	 dept_id numeric(2,0),
	 email    varchar(30),
	 primary key (user_id)
	);
	drop table instructor cascade;
create table department
	(dept_id	numeric(2,0),
	dept_name		varchar(20), 
	primary key (dept_id)
	);
create table course
	(course_id		varchar(8), 
	 title			varchar(50), 
	 dept_id		numeric(2,0),
	 rating numeric(3,2) check (rating>0.0and rating<6.0),
	 primary key (course_id),
	 foreign key (dept_id) references department
		on delete set null
	);
insert into department values(1,'Comp. Sci.');
insert into course values('CS 101','Introduction to programming',1,0.1);
insert into course values('CS 152','Paradigms of programming',1,0.1);
insert into course values('CS 154','Paradigms of programming lab',1,0.1);
insert into course values('CS 213','Data Structures and algorithms',1,0.1);
create table instructor
	(instructor_id			varchar(5), 
	 name			varchar(20) not null, 
	 dept_id		numeric(2,0), 
	 rating numeric(3,2) check (rating>0 and rating<6),
	 primary key (instructor_id),
	 foreign key (dept_id) references department
		on delete set null
	);
insert into instructor values(123,'Abhiram Ranade',1,0.01);
insert into instructor values(124,'Amitabh Sanyal',1,0.01);
insert into instructor values(125,'Ajit.A.Diwan',1,0.01);
