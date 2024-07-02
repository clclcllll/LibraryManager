-- 插入数据到 publishers 表
INSERT INTO publishers (name, address) VALUES 
('Pearson', '221 River St, Hoboken, NJ 07030, USA'),
('McGraw-Hill', '1325 Avenue of the Americas, New York, NY 10019, USA'),
('O\'Reilly Media', '1005 Gravenstein Highway North, Sebastopol, CA 95472, USA'),
('Penguin Random House', '1745 Broadway, New York, NY 10019, USA'),
('HarperCollins', '195 Broadway, New York, NY 10007, USA'),
('Addison-Wesley', '75 Arlington Street, Suite 300, Boston, MA 02116, USA'),
('Springer', '233 Spring St, New York, NY 10013, USA'),
('Cambridge University Press', 'University Printing House, Shaftesbury Road, Cambridge, CB2 8BS, UK'),
('Oxford University Press', 'Great Clarendon Street, Oxford, OX2 6DP, UK'),
('MIT Press', 'One Rogers Street, Cambridge, MA 02142, USA'),
('Prentice Hall', '221 River St, Hoboken, NJ 07030, USA');

-- 插入数据到 users 表
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin', 'admin'),
('librarian', 'librarian', 'librarian'),
('user1', 'user1', 'user'),
('user2', 'user2', 'user'),
('user3', 'user3', 'user'),
('user4', 'user4', 'user'),
('user5', 'user5', 'user');

-- 插入数据到 books 表
INSERT INTO books (title, author, isbn, publisher) VALUES 
('Effective Java', 'Joshua Bloch', '9780134685991', 'Pearson'),
('Clean Code', 'Robert C. Martin', '9780132350884', 'Pearson'),
('Design Patterns', 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', '9780201633610', 'Addison-Wesley'),
('The Pragmatic Programmer', 'Andrew Hunt, David Thomas', '9780201616224', 'Addison-Wesley'),
('Head First Design Patterns', 'Eric Freeman, Bert Bates, Kathy Sierra, Elisabeth Robson', '9780596007126', 'O\'Reilly Media'),
('Java Concurrency in Practice', 'Brian Goetz', '9780321349606', 'Addison-Wesley'),
('Introduction to Algorithms', 'Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein', '9780262033848', 'MIT Press'),
('Artificial Intelligence: A Modern Approach', 'Stuart Russell, Peter Norvig', '9780136042594', 'Pearson'),
('Database System Concepts', 'Abraham Silberschatz, Henry F. Korth, S. Sudarshan', '9780073523323', 'McGraw-Hill'),
('Computer Networks', 'Andrew S. Tanenbaum, David J. Wetherall', '9780132126953', 'Pearson'),
('Modern Operating Systems', 'Andrew S. Tanenbaum, Herbert Bos', '9780133591623', 'Pearson'),
('C Programming Language', 'Brian W. Kernighan, Dennis M. Ritchie', '9780131103627', 'Prentice Hall'),
('You Don\'t Know JS', 'Kyle Simpson', '9781491904244', 'O\'Reilly Media'),
('Python Crash Course', 'Eric Matthes', '9781593276034', 'No Starch Press'),
('Learning SQL', 'Alan Beaulieu', '9780596520830', 'O\'Reilly Media'),
('Refactoring', 'Martin Fowler', '9780201485677', 'Addison-Wesley'),
('Code Complete', 'Steve McConnell', '9780735619678', 'Microsoft Press'),
('The Mythical Man-Month', 'Frederick P. Brooks Jr.', '9780201835953', 'Addison-Wesley'),
('Domain-Driven Design', 'Eric Evans', '9780321125217', 'Addison-Wesley'),
('Cracking the Coding Interview', 'Gayle Laakmann McDowell', '9780984782857', 'CareerCup');

-- 插入数据到 available_books 表（部分书籍可借）
INSERT INTO available_books (id, title, author, isbn) SELECT id, title, author, isbn FROM books WHERE id IN (1, 3, 5, 7, 9, 11, 13, 15, 17, 19);

-- 插入数据到 borrowings 表（部分书籍已借出，确保一个用户只能借一本书）
INSERT INTO borrowings (book_id, username, borrowing_date) VALUES 
(2, 'user1', CURDATE()),
(4, 'user2', CURDATE()),
(6, 'user3', CURDATE()),
(8, 'user4', CURDATE()),
(10, 'user5', CURDATE());

-- 插入数据到 reservations 表
INSERT INTO reservations (book_id, username, reservation_date) VALUES 
(1, 'user1', '2024-06-01'),
(3, 'user2', '2024-06-02'),
(5, 'user3', '2024-06-03'),
(7, 'user4', '2024-06-04'),
(9, 'user5', '2024-06-05'),
(11, 'user1', '2024-06-06'),
(13, 'user2', '2024-06-07'),
(15, 'user3', '2024-06-08'),
(17, 'user4', '2024-06-09'),
(19, 'user5', '2024-06-10');
