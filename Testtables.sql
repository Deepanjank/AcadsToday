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
create table newsfeed(
 news_id varchar(20),
 user_id varchar(15),
 course_id varchar(8),
 news_text varchar(100),
 date_stamp date,
 time_stamp time,
 primary key(news_id),
 foreign key(user_id) references student,
 foreign key(course_id) references course
);

create table newstag(
 news_id varchar(20),
 tag varchar(20),
 primary key(news_id,tag),
 foreign key(news_id) references newsfeed
);
create table coursereview(
 review_id varchar(20),
 course_id varchar(20),
 user_id varchar(15),
 review_text varchar(500),
 downvotes numeric(4),
 upvotes numeric(4),
 date_stamp date,
 time_stamp time,
 
 primary key(review_id),
 foreign key(course_id) references course,
 foreign key(user_id) references student
);
create table instructorreview(
 review_id varchar(20),
 instructor_id varchar(20),
 user_id varchar(15),
 review_text varchar(500),
 downvotes numeric(4),
 upvotes numeric(4),
 date_stamp date,
 time_stamp time,
 primary key(review_id),
 foreign key(instructor_id) references instructor,
 foreign key(user_id) references student
);
create table follow(
user_id varchar(15),
course_id varchar(15),
primary key(user_id,course_id),
foreign key(user_id) references student,
foreign key(course_id) references course
);
create table instructorrating(
user_id varchar(15),
instructor_id varchar(20),
rating numeric(1) check (rating>0 and rating<6),
primary key(user_id,instructor_id),
foreign key(user_id) references student,
foreign key(instructor_id) references instructor
);
create table courserating(
user_id varchar(15),
course_id varchar(20),
rating numeric(1) check (rating>0 and rating<6),
primary key(user_id,course_id),
foreign key(user_id) references student,
foreign key(course_id) references course
);
create table instructorvotes(
user_id varchar(15),
review_id varchar(20),
UporDown boolean,
primary key(user_id,review_id),
foreign key(user_id) references student,
foreign key(review_id) references instructorreview
);
create table coursevotes(
user_id varchar(15),
review_id varchar(20),
UporDown boolean,
primary key(user_id,review_id),
foreign key(user_id) references student,
foreign key(review_id) references coursereview
);
drop table material;
create table material(
material_id varchar(20),
course_id  varchar(20),
user_id   varchar(20),
materialname varchar(20),
description varchar(100),
material   bytea,
date_stamp date,
time_stamp time,
primary key(material_id),
foreign key(course_id) references course,
foreign key(user_id) references student
);
create table coursecomments(
comment_id varchar(20),
review_id varchar(20),
user_id   varchar(20),
comment_text   varchar(100),
date_stamp date,
time_stamp time,
primary key(comment_id),
foreign key(review_id) references coursereview,
foreign key(user_id) references student
);
create table instructorcomments(
comment_id varchar(20),
review_id varchar(20),
user_id   varchar(20),
comment_text   varchar(100),
date_stamp date,
time_stamp time,
primary key(comment_id),
foreign key(review_id) references instructorreview,
foreign key(user_id) references student
);
create index on Student(user_id);
create index on follow(user_id);
create index on coursereview(user_id);
create index on coursereview(course_id);
create index on coursevotes(review_id,user_id);
create index on coursecomments(review_id);
create index on instructorreview(user_id);
create index on instructorreview(instructor_id);
create index on instructorcomments(review_id);
create index on instructorvotes(review_id,user_id);
create index on department(dept_id);
create index on instructor(instructor_id);
create index on instructorrating(instructor_id,user_id);
create index on course(course_id);
create index on courserating(course_id,user_id);
create index on material(user_id,course_id);
create index on newsfeed(course_id,date_stamp,time_stamp);
create index on newstag(tag);












