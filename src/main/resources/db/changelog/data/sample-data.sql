--liquibase formatted sql

--changeset codex:001-seed-category
INSERT INTO category (id, category_name, category_img, is_deleted)
VALUES
    (1001, 'Programming', 'https://res.cloudinary.com/demo/image/upload/programming.png', false),
    (1002, 'Business', 'https://res.cloudinary.com/demo/image/upload/business.png', false),
    (1003, 'Literature', 'https://res.cloudinary.com/demo/image/upload/literature.png', false),
    (1004, 'Science', 'https://res.cloudinary.com/demo/image/upload/science.png', false),
    (1005, 'Children', 'https://res.cloudinary.com/demo/image/upload/children.png', false),
    (1006, 'Self Improvement', 'https://res.cloudinary.com/demo/image/upload/self-improvement.png', false)
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), category_img = VALUES(category_img);
--rollback DELETE FROM category WHERE id BETWEEN 1001 AND 1006;

--changeset codex:002-seed-book
INSERT INTO book (
    id,
    title,
    category_id,
    author_id,
    price,
    average_rating,
    book_description,
    language,
    image_path,
    isbn,
    page,
    publisher,
    reprint,
    stock,
    sold,
    published_date,
    is_deleted
)
VALUES
    (1001, 'Clean Code', 1001, NULL, 320000, 4.8, 'A practical guide to writing readable and maintainable code.', 'English', 'https://res.cloudinary.com/demo/image/upload/clean-code.jpg', '9780132350884', 464, 'Prentice Hall', 1, 0, 0, '2008-08-01', false),
    (1002, 'Effective Java', 1001, NULL, 410000, 4.9, 'Best practices for Java programming and API design.', 'English', 'https://res.cloudinary.com/demo/image/upload/effective-java.jpg', '9780134685991', 416, 'Addison-Wesley', 3, 0, 0, '2018-01-06', false),
    (1003, 'Designing Data-Intensive Applications', 1001, NULL, 520000, 4.9, 'Core ideas behind reliable, scalable, and maintainable systems.', 'English', 'https://res.cloudinary.com/demo/image/upload/ddia.jpg', '9781449373320', 616, 'OReilly Media', 1, 0, 0, '2017-03-16', false),
    (1004, 'The Lean Startup', 1002, NULL, 260000, 4.5, 'A method for building products and businesses through validated learning.', 'English', 'https://res.cloudinary.com/demo/image/upload/lean-startup.jpg', '9780307887894', 336, 'Crown Business', 1, 0, 0, '2011-09-13', false),
    (1005, 'Good to Great', 1002, NULL, 280000, 4.6, 'Research-based principles behind durable business performance.', 'English', 'https://res.cloudinary.com/demo/image/upload/good-to-great.jpg', '9780066620992', 320, 'HarperBusiness', 1, 0, 0, '2001-10-16', false),
    (1006, 'To Kill a Mockingbird', 1003, NULL, 180000, 4.8, 'A classic novel about justice, empathy, and moral courage.', 'English', 'https://res.cloudinary.com/demo/image/upload/mockingbird.jpg', '9780061120084', 336, 'Harper Perennial', 1, 0, 0, '1960-07-11', false),
    (1007, 'The Great Gatsby', 1003, NULL, 150000, 4.4, 'A portrait of ambition and disillusionment in the Jazz Age.', 'English', 'https://res.cloudinary.com/demo/image/upload/gatsby.jpg', '9780743273565', 180, 'Scribner', 1, 0, 0, '1925-04-10', false),
    (1008, 'A Brief History of Time', 1004, NULL, 240000, 4.7, 'An accessible introduction to cosmology and the nature of time.', 'English', 'https://res.cloudinary.com/demo/image/upload/brief-history-time.jpg', '9780553380163', 212, 'Bantam', 1, 0, 0, '1988-04-01', false),
    (1009, 'The Selfish Gene', 1004, NULL, 230000, 4.6, 'A gene-centered view of evolution and natural selection.', 'English', 'https://res.cloudinary.com/demo/image/upload/selfish-gene.jpg', '9780198788607', 496, 'Oxford University Press', 4, 0, 0, '1976-03-13', false),
    (1010, 'Charlotte''s Web', 1005, NULL, 120000, 4.8, 'A beloved story about friendship, kindness, and growing up.', 'English', 'https://res.cloudinary.com/demo/image/upload/charlottes-web.jpg', '9780064400558', 192, 'HarperCollins', 1, 0, 0, '1952-10-15', false),
    (1011, 'Atomic Habits', 1006, NULL, 260000, 4.8, 'Small behavior changes that compound into meaningful personal improvement.', 'English', 'https://res.cloudinary.com/demo/image/upload/atomic-habits.jpg', '9780735211292', 320, 'Avery', 1, 0, 0, '2018-10-16', false),
    (1012, 'Deep Work', 1006, NULL, 250000, 4.6, 'Rules for focused success in a distracted world.', 'English', 'https://res.cloudinary.com/demo/image/upload/deep-work.jpg', '9781455586691', 304, 'Grand Central Publishing', 1, 0, 0, '2016-01-05', false)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    category_id = VALUES(category_id),
    price = VALUES(price),
    average_rating = VALUES(average_rating),
    book_description = VALUES(book_description),
    language = VALUES(language),
    image_path = VALUES(image_path),
    isbn = VALUES(isbn),
    page = VALUES(page),
    publisher = VALUES(publisher),
    reprint = VALUES(reprint),
    published_date = VALUES(published_date);
--rollback DELETE FROM book WHERE id BETWEEN 1001 AND 1012;

--changeset codex:003-seed-store
INSERT INTO store (id, store_name, phone_number, email, address_detail, active, is_deleted)
VALUES
    (1001, 'Downtown Book Hub', '0901001001', 'downtown@example.com', '12 Nguyen Hue, District 1', true, false),
    (1002, 'University Book Corner', '0901001002', 'university@example.com', '45 Le Thanh Ton, District 1', true, false),
    (1003, 'Family Reading Store', '0901001003', 'family@example.com', '88 Nguyen Trai, District 5', true, false)
ON DUPLICATE KEY UPDATE
    store_name = VALUES(store_name),
    phone_number = VALUES(phone_number),
    email = VALUES(email),
    address_detail = VALUES(address_detail),
    active = VALUES(active);
--rollback DELETE FROM store WHERE id BETWEEN 1001 AND 1003;

--changeset codex:004-seed-store-book
INSERT INTO store_book (store_id, book_id, stock, price, active, is_deleted)
VALUES
    (1001, 1001, 25, 315000, true, false),
    (1001, 1002, 18, 405000, true, false),
    (1001, 1003, 12, 520000, true, false),
    (1001, 1004, 20, 260000, true, false),
    (1001, 1006, 15, 180000, true, false),
    (1001, 1011, 30, 255000, true, false),
    (1002, 1001, 10, 320000, true, false),
    (1002, 1002, 16, 410000, true, false),
    (1002, 1003, 20, 510000, true, false),
    (1002, 1008, 22, 240000, true, false),
    (1002, 1009, 14, 230000, true, false),
    (1002, 1012, 24, 245000, true, false),
    (1003, 1005, 18, 275000, true, false),
    (1003, 1006, 28, 175000, true, false),
    (1003, 1007, 20, 150000, true, false),
    (1003, 1010, 40, 115000, true, false),
    (1003, 1011, 16, 260000, true, false),
    (1003, 1012, 12, 250000, true, false)
ON DUPLICATE KEY UPDATE
    stock = VALUES(stock),
    price = VALUES(price),
    active = VALUES(active);
--rollback DELETE FROM store_book WHERE store_id BETWEEN 1001 AND 1003 AND book_id BETWEEN 1001 AND 1012;
