-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 10, 2025 at 06:08 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";



/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bookbook`
--

-- --------------------------------------------------------

--
-- Table structure for table `author`
--

CREATE TABLE `author` (
  `id` binary(16) NOT NULL,
  `biography` varchar(255) DEFAULT NULL,
  `birth_date` datetime(6) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `author_name` varchar(20) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `author`
--

INSERT INTO `author` (`id`, `biography`, `birth_date`, `country`, `email`, `image_path`, `author_name`, `website`) VALUES
(0x1a5bc9fe184811f09d2700155d851915, 'Stephen William Hawking là một nhà vật lý lý thuyết, nhà vũ trụ học, tác giả khoa học nổi tiếng người Anh. Ông là giám đốc nghiên cứu tại Trung tâm Vũ trụ học lý thuyết thuộc Đại học Cambridge.', '1942-01-08 00:00:00.000000', 'Anh', 'contact@hawking.org.uk', '/images/authors/stephen_hawking.jpg', 'Stephen Hawking', 'www.hawkingfoundation.org'),
(0x1a5bdbd5184811f09d2700155d851915, 'Thomas Loren Friedman là một nhà báo và tác giả người Mỹ. Ông là ba lần người đoạt giải Pulitzer và hiện là nhà bình luận thường trực cho The New York Times.', '1953-07-20 00:00:00.000000', 'Mỹ', 'friedman@nytimes.com', '/images/authors/thomas_friedman.jpg', 'Thomas L. Friedman', 'www.thomaslfriedman.com'),
(0x1a5bdc79184811f09d2700155d851915, 'Dale Breckenridge Carnegie là một nhà văn và nhà thuyết trình Mỹ và là người phát triển các khóa học về tự giáo dục, nghệ thuật bán hàng, huấn luyện đoàn thể, nói trước công chúng và các kỹ năng giao tiếp giữa mọi người.', '1888-11-24 00:00:00.000000', 'Mỹ', 'info@dalecarnegie.com', '/images/authors/dale_carnegie.jpg', 'Dale Carnegie', 'www.dalecarnegie.com'),
(0x1a5bdcb9184811f09d2700155d851915, 'Stephen Richards Covey là một nhà giáo dục, tác giả, doanh nhân, và diễn giả người Mỹ. Ông nổi tiếng nhất với cuốn sách \"7 Thói Quen Của Người Thành Đạt\".', '1932-10-24 00:00:00.000000', 'Mỹ', 'info@stephencovey.com', '/images/authors/stephen_covey.jpg', 'Stephen R. Covey', 'www.stephencovey.com'),
(0x1a5bdd0e184811f09d2700155d851915, 'Nam Cao (tên thật là Trần Hữu Trí) là một nhà văn, nhà báo hiện thực xuất sắc của Việt Nam. Ông được biết đến với nhiều tác phẩm văn học có giá trị như \"Chí Phèo\", \"Lão Hạc\".', '1917-10-29 00:00:00.000000', 'Việt Nam', NULL, '/images/authors/nam_cao.jpg', 'Nam Cao', NULL),
(0x1a5bddb1184811f09d2700155d851915, 'Ngô Tất Tố là nhà văn, nhà báo, nhà nghiên cứu văn học và dịch giả người Việt Nam. Ông là tác giả của nhiều tác phẩm nổi tiếng như \"Tắt Đèn\", \"Việc Làng\".', '1894-01-01 00:00:00.000000', 'Việt Nam', NULL, '/images/authors/ngo_tat_to.jpg', 'Ngô Tất Tố', NULL),
(0x1a5bdded184811f09d2700155d851915, 'Vũ Trọng Phụng là nhà văn, nhà báo nổi tiếng của Việt Nam trong phong trào Tự lực văn đoàn. Ông được mệnh danh là \"Ông vua phóng sự đất Bắc\" với các tác phẩm như \"Số Đỏ\", \"Giông Tố\".', '1912-10-20 00:00:00.000000', 'Việt Nam', NULL, '/images/authors/vu_trong_phung.jpg', 'Vũ Trọng Phụng', NULL),
(0x1a5bde22184811f09d2700155d851915, 'Tô Hoài (tên thật là Nguyễn Sen) là nhà văn Việt Nam nổi tiếng với nhiều tác phẩm dành cho thiếu nhi và người lớn. Tác phẩm \"Dế Mèn Phiêu Lưu Ký\" của ông đã trở thành một tác phẩm kinh điển trong văn học thiếu nhi Việt Nam.', '1920-09-27 00:00:00.000000', 'Việt Nam', NULL, '/images/authors/to_hoai.jpg', 'Tô Hoài', NULL),
(0x1a5bde5c184811f09d2700155d851915, 'Tetsuko Kuroyanagi là một nữ diễn viên, tác giả và đại sứ thiện chí UNICEF người Nhật Bản. Bà được biết đến rộng rãi với tác phẩm tự truyện \"Totto-chan: Cô Bé Bên Cửa Sổ\".', '1933-08-09 00:00:00.000000', 'Nhật Bản', 'tetsuko@example.jp', '/images/authors/tetsuko_kuroyanagi.jpg', 'Tetsuko Kuroyanagi', 'www.tetsukokuroyanagi.jp'),
(0x1a5bde91184811f09d2700155d851915, 'Tập hợp các tác giả khác nhau đóng góp vào các tuyển tập hoặc tác phẩm tổng hợp.', NULL, NULL, NULL, '/images/authors/various_authors.jpg', 'Nhiều Tác Giả', NULL),
(0x1a5bdec2184811f09d2700155d851915, 'Joanne Rowling, được biết đến với bút danh J.K. Rowling, là nhà văn, nhà từ thiện, nhà sản xuất phim và truyền hình người Anh. Bà nổi tiếng với bộ tiểu thuyết Harry Potter.', '1965-07-31 00:00:00.000000', 'Anh', 'contact@jkrowling.com', '/images/authors/jk_rowling.jpg', 'J.K. Rowling', 'www.jkrowling.com'),
(0x1a5bdef8184811f09d2700155d851915, 'John Ronald Reuel Tolkien là nhà văn, nhà thơ, nhà ngôn ngữ học và giáo sư đại học người Anh. Ông nổi tiếng với các tác phẩm giả tưởng kinh điển như \"Chúa Tể Những Chiếc Nhẫn\" và \"Người Hobbit\".', '1892-01-03 00:00:00.000000', 'Anh', NULL, '/images/authors/jrr_tolkien.jpg', 'J.R.R. Tolkien', 'www.tolkienestate.com'),
(0x1a5bdf33184811f09d2700155d851915, 'Richard Russell Riordan Jr. là một tác giả người Mỹ nổi tiếng với nhiều loạt sách giả tưởng dựa trên thần thoại, đặc biệt là loạt sách \"Percy Jackson và các vị thần trên đỉnh Olympus\".', '1964-06-05 00:00:00.000000', 'Mỹ', 'info@rickriordan.com', '/images/authors/rick_riordan.jpg', 'Rick Riordan', 'www.rickriordan.com'),
(0x1a5bdf6b184811f09d2700155d851915, 'Robert Toru Kiyosaki là một doanh nhân, nhà đầu tư và tác giả người Mỹ. Ông nổi tiếng với cuốn sách \"Cha Giàu, Cha Nghèo\" và loạt sách \"Dạy Con Làm Giàu\".', '1947-04-08 00:00:00.000000', 'Mỹ', 'support@richdad.com', '/images/authors/robert_kiyosaki.jpg', 'Robert T. Kiyosaki', 'www.richdad.com'),
(0x1a5bdf9d184811f09d2700155d851915, 'T. Harv Eker là một tác giả, doanh nhân và diễn giả động viên người Canada-Mỹ. Ông nổi tiếng với cuốn sách \"Bí Mật Tư Duy Triệu Phú\".', '1954-06-10 00:00:00.000000', 'Mỹ', 'info@harveker.com', '/images/authors/harv_eker.jpg', 'T. Harv Eker', 'www.harveker.com'),
(0x1a5bec73184811f09d2700155d851915, 'Trần Trọng Kim là một nhà giáo dục, học giả, nhà văn và chính trị gia Việt Nam. Ông nổi tiếng với tác phẩm \"Việt Nam Sử Lược\" và từng là Thủ tướng Chính phủ Đế quốc Việt Nam năm 1945.', '1883-01-01 00:00:00.000000', 'Việt Nam', NULL, '/images/authors/tran_trong_kim.jpg', 'Trần Trọng Kim', NULL),
(0x1a5becc8184811f09d2700155d851915, 'Plato là một triết gia Hy Lạp cổ đại, người sáng lập Học viện Athens và là tác giả của các đối thoại triết học bao gồm \"Chính Trị Luận\". Ông được coi là một trong những triết gia có ảnh hưởng nhất trong lịch sử phương Tây.', '1883-01-01 00:00:00.000000', 'Hy Lạp', NULL, '/images/authors/plato.jpg', 'Plato', NULL),
(0x1a5becf3184811f09d2700155d851915, 'Phạm Lữ Ân là bút danh của một tác giả Việt Nam, nổi tiếng với những tác phẩm tình cảm, triết lý sống như \"Nếu Biết Trăm Năm Là Hữu Hạn\".', '1888-11-24 00:00:00.000000', 'Việt Nam', NULL, '/images/authors/pham_lu_an.jpg', 'Phạm Lữ Ân', NULL),
(0x1a5bed14184811f09d2700155d851915, 'Tân Di Ổ là một nhà văn người Trung Quốc, nổi tiếng với nhiều tiểu thuyết tình cảm, lãng mạn được độc giả Việt Nam yêu thích như \"Anh Có Thích Nước Mỹ Không?\".', '1888-11-24 00:00:00.000000', 'Trung Quốc', NULL, '/images/authors/tan_di_o.jpg', 'Tân Di Ổ', NULL),
(0x1a5bed33184811f09d2700155d851915, 'Cố Mạn là một nhà văn nữ người Trung Quốc, nổi tiếng với các tiểu thuyết ngôn tình như \"Bên Nhau Trọn Đời\", \"Sam Sam Đến Rồi\".', '1981-01-01 00:00:00.000000', 'Trung Quốc', NULL, '/images/authors/co_man.jpg', 'Cố Mạn', NULL),
(0x1a5bed51184811f09d2700155d851915, 'Nguyễn Dzoãn Cẩm Vân là một đầu bếp nổi tiếng Việt Nam, được biết đến qua nhiều chương trình dạy nấu ăn trên truyền hình và sách dạy nấu ăn như \"365 Món Ngon Mỗi Ngày\".', '1951-01-01 00:00:00.000000', 'Việt Nam', 'camvan@example.com', '/images/authors/nguyen_dzoan_cam_van.jpg', 'Nguyễn Dzoãn Cẩm Vân', NULL),
(0x1a5bed73184811f09d2700155d851915, 'Julia Child là một đầu bếp, tác giả và người dẫn chương trình truyền hình Mỹ. Bà nổi tiếng với việc giới thiệu ẩm thực Pháp đến công chúng Mỹ thông qua các cuốn sách và chương trình truyền hình của mình.', '1912-08-15 00:00:00.000000', 'Mỹ', NULL, '/images/authors/julia_child.jpg', 'Julia Child', NULL),
(0x1a5bed92184811f09d2700155d851915, 'Hiromi Shinya là một bác sĩ người Nhật Bản, chuyên về nội soi đại tràng. Ông nổi tiếng với các cuốn sách về sức khỏe và chế độ ăn uống như \"Nhân Tố Enzyme\" và \"Sống Lâu Trăm Tuổi\".', '1935-01-01 00:00:00.000000', 'Nhật Bản', NULL, '/images/authors/hiromi_shinya.jpg', 'Hiromi Shinya', 'www.shinya.jp'),
(0x1a5bedb1184811f09d2700155d851915, 'Huyền Chip (tên thật là Phạm Thị Huệ) là một cô gái trẻ Việt Nam nổi tiếng với cuốn sách \"Xách Ba Lô Lên Và Đi\" kể về hành trình của cô đi qua 25 quốc gia.', '1990-01-01 00:00:00.000000', 'Việt Nam', 'huyenchip@example.com', '/images/authors/huyen_chip.jpg', 'Huyền Chip', 'www.huyenchip.com'),
(0x1a5bedcf184811f09d2700155d851915, 'Trang Hạ là bút danh của một nhà văn, nhà báo Việt Nam. Cô nổi tiếng với các bài viết, tiểu luận và du ký như \"Tuổi Trẻ Trong Những Chuyến Đi\".', '1975-01-01 00:00:00.000000', 'Việt Nam', NULL, '/images/authors/trang_ha.jpg', 'Trang Hạ', 'www.trangha.com'),
(0x1a5beded184811f09d2700155d851915, 'Phan Việt là một nhà văn, dịch giả Việt Nam. Cô nổi tiếng với cuốn du ký \"Một Mình Ở Châu Âu\" và được độc giả yêu mến với lối viết chân thành, cảm xúc.', '1888-11-24 00:00:00.000000', 'Việt Nam', NULL, '/images/authors/phan_viet.jpg', 'Phan Việt', NULL),
(0x1a5bee0b184811f09d2700155d851915, 'Phạm Văn Ất là một giáo sư, nhà giáo, chuyên gia về Toán học và Công nghệ thông tin tại Việt Nam. Ông là tác giả của nhiều giáo trình, sách tham khảo về Toán học, Tin học được sử dụng rộng rãi trong các trường đại học.', '1940-01-01 00:00:00.000000', 'Việt Nam', NULL, '/images/authors/pham_van_at.jpg', 'Phạm Văn Ất', NULL),
(0x1a5bee29184811f09d2700155d851915, 'Nguyễn Đình Trí là một giáo sư, nhà toán học Việt Nam. Ông là tác giả của nhiều giáo trình, sách tham khảo về Toán học cao cấp và Đại số tuyến tính được sử dụng trong giảng dạy ở các trường đại học.', '1930-01-01 00:00:00.000000', 'Việt Nam', NULL, '/images/authors/nguyen_dinh_tri.jpg', 'Nguyễn Đình Trí', NULL),
(0x1a5bee49184811f09d2700155d851915, 'Nguyễn Văn Hiệp là một chuyên gia về công nghệ thông tin, tác giả của nhiều giáo trình, sách tham khảo về Cơ sở dữ liệu và Hệ thống thông tin được sử dụng trong các trường đại học Việt Nam.', '1960-01-01 00:00:00.000000', 'Việt Nam', NULL, '/images/authors/nguyen_van_hiep.jpg', 'Nguyễn Văn Hiệp', NULL),
(0x299980d0e78243ecab3b114836fed6d8, NULL, NULL, NULL, NULL, NULL, 'Phùng Quán', NULL),
(0x29cb2c4f19734582850365b42223a609, NULL, NULL, NULL, NULL, NULL, 'Đoàn Giỏi', NULL),
(0x3925276b245947f3946f640457e674bc, NULL, NULL, NULL, NULL, NULL, 'Andy Weir', NULL),
(0x54fb3e59202f40259ef1f769443fdab9, NULL, NULL, NULL, NULL, NULL, 'Nguyễn Nhật Ánh', NULL),
(0xaac59b3132e711f082a900155d7083ad, 'Kim Lân là một nhà văn nổi tiếng Việt Nam, nổi bật với các tác phẩm viết về nông thôn và con người Việt Nam sau Cách mạng tháng Tám.', '1920-08-01 00:00:00.000000', 'Việt Nam', 'kimlan@example.com', '/images/authors/kim_lan.jpg', 'Kim Lân', ''),
(0xe568a023c3934f609c0bd509fdbee660, NULL, NULL, NULL, NULL, NULL, 'Mary Robinette Kowal', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `id` binary(16) NOT NULL,
  `average_rating` double DEFAULT NULL,
  `book_description` text DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `isbn` varchar(255) NOT NULL,
  `language` varchar(255) DEFAULT NULL,
  `page` int(11) DEFAULT NULL,
  `price` bigint(20) NOT NULL,
  `published_date` date NOT NULL,
  `publisher` varchar(255) NOT NULL,
  `reprint` int(11) DEFAULT NULL,
  `sold` bigint(20) NOT NULL,
  `stock` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `author_id` binary(16) DEFAULT NULL,
  `category_id` binary(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`id`, `average_rating`, `book_description`, `image_path`, `isbn`, `language`, `page`, `price`, `published_date`, `publisher`, `reprint`, `sold`, `stock`, `title`, `author_id`, `category_id`) VALUES
(0x18818805b39b479dbfbe31b0ed5b7f2b, 5, 'Lấy bối cảnh giả tưởng vào năm 1952, khi một thiên thạch khổng lồ đâm xuống Trái Đất, tàn phá phần lớn Bờ Đông Hoa Kỳ và dẫn đến biến đổi khí hậu toàn cầu. Trước nguy cơ tuyệt chủng, loài người phải gấp rút phát triển chương trình không gian để di cư ra ngoài hành tinh. Cuốn tiểu thuyết kết hợp giữa khoa học viễn tưởng, chính trị, và tâm lý nhân vật, xoay quanh Elma York – một nữ phi công tài năng, người không chỉ chiến đấu cho sự sống còn của nhân loại mà còn chống lại định kiến giới tính để trở thành người phụ nữ đầu tiên bay vào vũ trụ.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1747068272/302800428-0_gaugev.jpg', '9780733426094', 'English', 432, 189000, '2018-07-03', 'Tor Books', 2, 2110, 66, 'The Calculating Stars', 0xe568a023c3934f609c0bd509fdbee660, 0x13e9528f184811f09d2700155d851915),
(0x1f79b19b184811f09d2700155d851915, 4.5, '“Vũ Trụ Trong Vỏ Hạt Dẻ” là một tác phẩm được viết bởi Stephen Hawking với mục đích đưa những kiến thức phức tạp về vật lý thiên văn và lý thuyết vũ trụ vào tầm hiểu của người đọc phổ thông. Cuốn sách khám phá những khái niệm như thuyết siêu dây, không thời gian uốn cong, và vũ trụ song song, tất cả đều được diễn giải qua các minh họa trực quan và ngôn ngữ thân thiện. Đây là cuốn sách không chỉ mở rộng tầm hiểu biết về vũ trụ mà còn làm thay đổi cách chúng ta nhìn nhận về thời gian, không gian và chính bản thân mình.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744088783/sg-11134201-7rd4f-m76dvxyictf683_cprsqn.webp', '9788837092467', 'Tiếng Việt', 248, 120000, '2022-01-01', 'NXB Trẻ', 5, 17, 3, 'Vũ Trụ Trong Vỏ Hạt Dẻ', 0x1a5bc9fe184811f09d2700155d851915, 0x13e93799184811f09d2700155d851915),
(0x1f79e2f9184811f09d2700155d851915, 4, '“A Brief History of Time” là kiệt tác nổi bật của Stephen Hawking, trình bày một cách đầy đủ và cô đọng nhất về nguồn gốc, cấu trúc và tương lai của vũ trụ. Từ Big Bang đến lỗ đen, từ thuyết tương đối đến thuyết lượng tử, cuốn sách là hành trình khám phá cách khoa học lý giải bản chất thực tại. Với lối viết dễ tiếp cận nhưng không làm mất đi chiều sâu tri thức, tác phẩm đã đưa hàng triệu người đọc trên thế giới đến gần hơn với những bí ẩn lớn nhất của vũ trụ.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744088852/vn-11134207-7r98o-lnhyg46hwmi559_resize_w900_nl_df6cht.webp', '9783161484100', 'English', 320, 130009, '2021-05-15', 'NXB Trẻ', 5, 31, 45, 'A Brief History of Time', 0x1a5bc9fe184811f09d2700155d851915, 0x13e93799184811f09d2700155d851915),
(0x1f79e4d6184811f09d2700155d851915, 3.5, '“Thế Giới Phẳng” là một công trình nghiên cứu sâu sắc của Thomas L. Friedman về quá trình toàn cầu hóa trong thế kỷ 21. Cuốn sách lý giải cách mà công nghệ, internet, và chuỗi cung ứng toàn cầu đã làm “phẳng” thế giới – xóa bỏ rào cản địa lý và mở ra cơ hội cạnh tranh bình đẳng hơn giữa các quốc gia và cá nhân. Tác phẩm không chỉ cung cấp kiến thức kinh tế vĩ mô mà còn là lời cảnh tỉnh cho mỗi người trước làn sóng thay đổi chóng mặt của thời đại số.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744088883/vn-11134207-7r98o-lw5l295wdihl47_resize_w900_nl_yvf8nx.webp', '9781782808084', 'Tiếng Việt', 512, 115000, '2020-10-10', 'NXB Trẻ', 5, 11, 7, 'Thế Giới Phẳng', 0x1a5bdbd5184811f09d2700155d851915, 0x13e93799184811f09d2700155d851915),
(0x1f79e5e3184811f09d2700155d851915, 4.8, '“Đắc Nhân Tâm” là cuốn sách kinh điển trong lĩnh vực phát triển bản thân và nghệ thuật giao tiếp. Dale Carnegie đưa ra những nguyên tắc bất biến về cách cư xử, thuyết phục và tạo ảnh hưởng đến người khác, thông qua hàng trăm câu chuyện thực tế và bài học thấm đẫm triết lý nhân văn. Đây là cẩm nang giúp người đọc xây dựng mối quan hệ tích cực, nâng cao khả năng lãnh đạo và phát triển sự nghiệp bền vững.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744088927/sg-11134201-7rd5d-m76eg2do92mhf2_zrhcor.webp', '9786045831123', 'Tiếng Việt', 320, 95000, '2022-03-12', 'NXB Tổng hợp TPHCM', 5, 28, 5, 'Đắc Nhân Tâm', 0x1a5bdc79184811f09d2700155d851915, 0x13e951fb184811f09d2700155d851915),
(0x1f79f426184811f09d2700155d851915, 4.5, '“Quẳng Gánh Lo Đi Và Vui Sống” mang đến cho độc giả những nguyên lý sống tích cực và phương pháp vượt qua lo âu, trầm cảm. Với những câu chuyện gần gũi và lời khuyên thiết thực, Dale Carnegie giúp người đọc học cách chấp nhận quá khứ, sống trọn vẹn ở hiện tại và hướng về tương lai với tinh thần lạc quan. Cuốn sách là nguồn động viên tinh thần mạnh mẽ, đặc biệt trong những giai đoạn áp lực cao của cuộc sống hiện đại.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744088944/sg-11134201-7rd5w-m76eg28ei8ij88_biylpb.webp', '9780123456786', 'Tiếng Việt', 296, 99000, '2021-07-20', 'NXB Tổng hợp TPHCM', 5, 23, 7, 'Quẳng Gánh Lo Đi Và Vui Sống', 0x1a5bdc79184811f09d2700155d851915, 0x13e951fb184811f09d2700155d851915),
(0x1f79f5af184811f09d2700155d851915, 4.4, '“Thói Quen Thứ 8” là tác phẩm tiếp nối thành công của “7 Thói Quen Hiệu Quả”, được viết bởi Stephen R. Covey nhằm giúp con người tìm kiếm tiếng nói nội tâm và truyền cảm hứng cho người khác cùng phát triển. Cuốn sách đề cập đến tầm quan trọng của việc khám phá mục đích sống, phát triển năng lực lãnh đạo cá nhân, và chuyển từ thành công cá nhân sang ảnh hưởng cộng đồng. Đây là hành trình chuyển hóa bản thân từ hiệu quả đến vĩ đại.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744088983/2bf74f825b367078b5a1e19073d2dec2_resize_w900_nl_fqxfld.webp', '9786045837520', 'Tiếng Việt', 410, 105000, '2021-02-28', 'NXB Tổng hợp TPHCM', 5, 25, 1, 'Thói Quen Thứ 8', 0x1a5bdcb9184811f09d2700155d851915, 0x13e951fb184811f09d2700155d851915),
(0x1f79f6b0184811f09d2700155d851915, 4.6, '“Chí Phèo” là tác phẩm kinh điển của nhà văn Nam Cao, phản ánh hiện thực xã hội phong kiến tàn bạo và bất công. Nhân vật Chí Phèo – từ một người nông dân lương thiện bị xã hội đẩy vào con đường tha hóa, trở thành biểu tượng cho bi kịch của con người khi bị chối bỏ quyền làm người. Tác phẩm sâu sắc về giá trị nhân bản và đặt ra câu hỏi lớn về sự tha hóa, đạo đức và lòng nhân ái.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744089017/vn-11134207-7ra0g-m6nr4tgwi1k706_resize_w900_nl_olqdel.webp', '9786045830128', 'Tiếng Việt', 175, 70000, '2022-04-10', 'NXB Văn học', 5, 43, 0, 'Chí Phèo', 0x1a5bdd0e184811f09d2700155d851915, 0x13e9525f184811f09d2700155d851915),
(0x1f79f7a5184811f09d2700155d851915, 4.5, '“Tắt Đèn” của Ngô Tất Tố là một tác phẩm hiện thực phê phán nổi bật, tái hiện cuộc sống khổ cực của người nông dân Việt Nam dưới ách thống trị phong kiến và thực dân. Nhân vật chị Dậu hiện lên như một biểu tượng của người phụ nữ kiên cường, chịu thương chịu khó nhưng bị dồn ép đến đường cùng. Cuốn sách là lời tố cáo đanh thép và chân thực về bất công xã hội, khơi dậy sự cảm thông và thức tỉnh lương tri.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744089044/vn-11134201-7ra0g-m6tg78psbewo36_resize_w900_nl_eewacl.webp', '9786045830357', 'Tiếng Việt', 220, 75000, '2021-08-15', 'NXB Văn học', 5, 21, 13, 'Tắt Đèn', 0x1a5bddb1184811f09d2700155d851915, 0x13e9525f184811f09d2700155d851915),
(0x1f79f89b184811f09d2700155d851915, 5, '“Số Đỏ” của Vũ Trọng Phụng là một tác phẩm trào phúng kinh điển, phản ánh sâu sắc sự lố lăng, nửa mùa và giả tạo của xã hội thượng lưu thành thị thời kỳ giao thời giữa phong kiến và hiện đại. Nhân vật Xuân Tóc Đỏ – một kẻ cơ hội và vô học, qua những tình huống dở khóc dở cười, trở thành người thành đạt nhờ vào sự ngớ ngẩn của xã hội xung quanh. Tác phẩm là màn biếm họa đầy châm biếm nhưng không kém phần trí tuệ.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748874960/l_455072028_a2d5109ed55d6c144e79a216fa03981d_qxscgk.jpg', '9786045830975', 'Tiếng Việt', 250, 80000, '2022-01-20', 'NXB Văn học', 5, 19, 14, 'Số Đỏ', 0x1a5bdded184811f09d2700155d851915, 0x13e9525f184811f09d2700155d851915),
(0x1f79f9b2184811f09d2700155d851915, 5, '“Dế Mèn Phiêu Lưu Ký” là một tác phẩm văn học thiếu nhi kinh điển của Tô Hoài, kể lại hành trình khám phá thế giới đầy màu sắc của chú dế Mèn. Trên đường đi, Dế Mèn gặp gỡ nhiều loài vật, chứng kiến những câu chuyện về tình bạn, lòng dũng cảm và cả sự hy sinh. Cuốn sách không chỉ hấp dẫn với trẻ em mà còn chứa đựng nhiều tầng lớp ý nghĩa sâu sắc về cuộc sống và sự trưởng thành.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744089197/sg-11134201-7rcf0-m6imjmu9pbzme2_resize_w900_nl_ionafw.webp', '9786045832463', 'Tiếng Việt', 185, 60000, '2022-06-01', 'NXB Kim Đồng', 5, 25, 33, 'Dế Mèn Phiêu Lưu Ký', 0x1a5bde22184811f09d2700155d851915, 0x13e95276184811f09d2700155d851915),
(0x1f79faa3184811f09d2700155d851915, 4.8, '“Totto-chan: Cô Bé Bên Cửa Sổ” là câu chuyện có thật về tuổi thơ của nữ tác giả Tetsuko Kuroyanagi tại một ngôi trường đặc biệt mang tên Tomoe. Thầy hiệu trưởng Kobayashi với triết lý giáo dục tiên phong đã nuôi dưỡng tâm hồn và sự sáng tạo của học sinh thông qua tình yêu thương, sự lắng nghe và tôn trọng. Cuốn sách là minh chứng xúc động cho một nền giáo dục nhân văn và khai phóng.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744089376/tottochan_the_little_girl_at_t_1668619682_e8b5b74b_lkragh.jpg', '9786045834215', 'Tiếng Việt', 265, 85000, '2021-09-25', 'NXB Hội Nhà văn', 5, 19, 31, 'Totto-chan: Cô Bé Bên Cửa Sổ', 0x1a5bde5c184811f09d2700155d851915, 0x13e95276184811f09d2700155d851915),
(0x1f79fb8d184811f09d2700155d851915, 4.6, 'Tuyển tập các câu chuyện cổ tích dân gian Việt Nam', '/images/books/coTichVietNam.jpg', '9786045831896', 'Tiếng Việt', 200, 50000, '2022-02-10', 'NXB Kim Đồng', 5, 25, 47, 'Những Câu Chuyện Cổ Tích Việt Nam', 0x1a5bde91184811f09d2700155d851915, 0x13e95276184811f09d2700155d851915),
(0x1f79fc81184811f09d2700155d851915, 4.9, '“Harry Potter và Hòn Đá Phù Thủy” là tập đầu tiên trong loạt truyện nổi tiếng của J.K. Rowling, mở đầu cho hành trình kỳ ảo tại Trường Phù thủy Hogwarts. Cậu bé Harry – vốn sống khổ cực với gia đình Dursley – bất ngờ phát hiện mình là phù thủy, kết bạn với Ron và Hermione, và đối đầu với bí ẩn xoay quanh hòn đá phù thủy và kẻ thù không đội trời chung – Voldemort.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744089513/sg-11134201-7rd5c-m76dx519bhf6a9_owlq94.webp', '9786045831236', 'Tiếng Việt', 366, 150000, '2022-07-31', 'NXB Trẻ', 5, 20, 25, 'Harry Potter và Hòn Đá Phù Thủy', 0x1a5bdec2184811f09d2700155d851915, 0x13e9528f184811f09d2700155d851915),
(0x1f79fd7e184811f09d2700155d851915, 4.8, '“The Lord of the Rings” là bộ sử thi giả tưởng vĩ đại của J.R.R. Tolkien, theo chân Frodo Baggins trong cuộc hành trình tiêu hủy chiếc nhẫn quyền năng để ngăn chặn thế lực bóng tối của Sauron. Với bối cảnh thế giới Trung Địa rộng lớn, tác phẩm là biểu tượng của lòng trung thành, sự hy sinh, và tinh thần anh hùng vượt thời gian.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744089566/OIP_fgz66b.jpg', '9786045832573', 'Tiếng Việt', 850, 200000, '2021-11-05', 'NXB Văn học', 5, 12, 23, 'The Lord of the Rings', 0x1a5bdef8184811f09d2700155d851915, 0x13e9528f184811f09d2700155d851915),
(0x1f79fe76184811f09d2700155d851915, 4.7, '“Percy Jackson” là loạt tiểu thuyết phiêu lưu giả tưởng hấp dẫn dành cho lứa tuổi thiếu niên, xoay quanh cuộc đời của cậu bé Percy – một á thần con trai của Poseidon. Khi phát hiện ra thân thế thật của mình, Percy bước vào thế giới các vị thần Hy Lạp cổ đại, chiến đấu với quái vật và thực hiện những sứ mệnh nguy hiểm để bảo vệ cả nhân loại lẫn thế giới thần linh.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744089645/vn-11134201-7r98o-lx3vleu5x1i17c_resize_w900_nl_qaxnvi.webp', '9786045833748', 'Tiếng Việt', 420, 135000, '2022-05-20', 'NXB Hội Nhà văn', 5, 20, 22, 'Percy Jackson', 0x1a5bdf33184811f09d2700155d851915, 0x13e9528f184811f09d2700155d851915),
(0x1f79ff75184811f09d2700155d851915, 4.6, '“Cha Giàu Cha Nghèo” là một cuốn sách kinh điển về tài chính cá nhân của Robert T. Kiyosaki, so sánh giữa hai tư duy tài chính trái ngược: của “cha nghèo” - người học cao nhưng luôn gặp khó khăn tài chính và “cha giàu” - người ít học nhưng thành công trong đầu tư và xây dựng tài sản. Qua đó, tác giả truyền tải những bài học quý báu về tư duy làm giàu, quản lý tiền bạc và đầu tư hiệu quả.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744089785/kiotviet_0d7e2251c670c3f1498f2f2b90681181_dgc1vj.jpg', '9786044834030', 'Tiếng Việt', 280, 99000, '2022-03-15', 'NXB Trẻ', 5, 14, 18, 'Cha Giàu Cha Nghèo (Rich Dad Poor Dad)', 0x1a5bdf6b184811f09d2700155d851915, 0x13e952aa184811f09d2700155d851915),
(0x1f7a0060184811f09d2700155d851915, 4, '“Bí Mật Tư Duy Triệu Phú” là cuốn sách truyền cảm hứng mạnh mẽ về tư duy tài chính và cách thay đổi thói quen suy nghĩ để đạt được sự giàu có. Tác giả T. Harv Eker chỉ ra sự khác biệt rõ rệt giữa người giàu và người nghèo, từ đó hướng dẫn cách tái lập trình tư duy để tạo ra thành công bền vững.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744089825/bi-mat-tu-duy-trieu-phu-a_mcetrq.jpg', '9786045831673', 'Tiếng Việt', 256, 105000, '2021-08-10', 'NXB Tổng hợp TPHCM', 5, 11, 12, 'Bí Mật Tư Duy Triệu Phú', 0x1a5bdf9d184811f09d2700155d851915, 0x13e952aa184811f09d2700155d851915),
(0x1f7a0146184811f09d2700155d851915, 5, '“Dạy Con Làm Giàu” là một phần trong loạt sách nổi tiếng của Robert T. Kiyosaki, tập trung vào việc giáo dục tài chính cho trẻ em từ nhỏ. Cuốn sách giúp các bậc phụ huynh truyền đạt kiến thức về quản lý tiền bạc, đầu tư và tư duy làm chủ tài chính một cách thực tế, dễ hiểu.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744089877/IMG_6619_mxrp7i.jpg', '9786045832748', 'Tiếng Việt', 230, 95000, '2022-01-10', 'NXB Trẻ', 5, 11, 17, 'Dạy Con Làm Giàu', 0x1a5bdf6b184811f09d2700155d851915, 0x13e952aa184811f09d2700155d851915),
(0x1f7a023b184811f09d2700155d851915, 4.7, '“Lược Sử Việt Nam” là cuốn sách khái quát lịch sử Việt Nam từ buổi đầu dựng nước đến thời kỳ hiện đại. Với văn phong ngắn gọn, mạch lạc và nội dung được sắp xếp theo tiến trình thời gian, tác phẩm giúp người đọc hình dung toàn cảnh các thời kỳ phát triển, thăng trầm và những dấu ấn lịch sử trọng đại của dân tộc.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744089900/viet-nam-su-luoc-sach-ls_ubgtlt.jpg', '9786045830586', 'Tiếng Việt', 420, 87000, '2021-10-20', 'NXB Văn học', 5, 10, 20, 'Lược Sử Việt Nam', 0x1a5bec73184811f09d2700155d851915, 0x13e952bf184811f09d2700155d851915),
(0x1f7a032c184811f09d2700155d851915, 4.6, '“Lịch Sử Thế Giới” là cuốn sách tổng hợp về quá trình hình thành và phát triển của nhân loại từ thời tiền sử đến hiện đại. Tác phẩm trình bày những sự kiện then chốt, các nền văn minh lớn và cuộc cách mạng ảnh hưởng sâu rộng đến thế giới loài người, giúp độc giả có cái nhìn toàn diện về lịch sử toàn cầu.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744090007/untitled-53-compressed.jpg_oegtfg.jpg', '9786045831532', 'Tiếng Việt', 650, 110000, '2022-02-18', 'NXB Giáo dục', 5, 8, 14, 'Lịch Sử Thế Giới', 0x1a5bde91184811f09d2700155d851915, 0x13e952bf184811f09d2700155d851915),
(0x1f7a041c184811f09d2700155d851915, 4.3, '“Chính Trị Luận” là tác phẩm triết học chính trị nổi tiếng của Plato, một trong những triết gia vĩ đại nhất Hy Lạp cổ đại. Cuốn sách bàn về bản chất của công lý, cấu trúc xã hội lý tưởng, và vai trò của tri thức trong quản lý nhà nước. Đây là nền tảng cho nhiều tư tưởng chính trị phương Tây sau này.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744090718/4d8328c53e74b03091c8198689facc9b_bkxxve.jpg', '9786045831967', 'Tiếng Việt', 310, 98000, '2021-06-15', 'NXB Chính trị Quốc gia', 5, 8, 8, 'Chính Trị Luận', 0x1a5becc8184811f09d2700155d851915, 0x13e952bf184811f09d2700155d851915),
(0x1f7a04ff184811f09d2700155d851915, 4.5, '“Nếu Biết Trăm Năm Là Hữu Hạn” là tuyển tập những bài viết sâu sắc, nhẹ nhàng nhưng đầy chiêm nghiệm về tình yêu, tuổi trẻ và sự hữu hạn của cuộc đời. Tác giả Phạm Lữ Ân khơi gợi cảm xúc qua từng trang sách, giúp người đọc đối diện với chính mình và trân trọng hơn những gì đang có.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744090752/fcd5d7c1e963e66f686061393793d9b8_xe1j2h.jpg', '9786045830623', 'Tiếng Việt', 248, 72000, '2022-02-14', 'NXB Hội Nhà văn', 5, 14, 24, 'Nếu Biết Trăm Năm Là Hữu Hạn', 0x1a5becf3184811f09d2700155d851915, 0x13e9536c184811f09d2700155d851915),
(0x1f7a05fe184811f09d2700155d851915, 4.4, '“Anh Có Thích Nước Mỹ Không?” là một câu chuyện tình yêu nhẹ nhàng nhưng đầy cảm xúc của tác giả Tân Di Ổ. Câu chuyện xoay quanh những lựa chọn và nuối tiếc của tuổi trẻ, khi tình yêu phải đối mặt với thực tế cuộc sống, khiến người đọc suy ngẫm về sự trưởng thành và những điều không thể quay lại.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748014662/OIP_rngf2m.jpg', '9786045833625', 'Tiếng Việt', 320, 98000, '2021-12-05', 'NXB Văn học', 5, 25, 100, 'Anh Có Thích Nước Mỹ Không?', 0x1a5bed14184811f09d2700155d851915, 0x13e9536c184811f09d2700155d851915),
(0x1f7a06f0184811f09d2700155d851915, 4.6, '“Bên Nhau Trọn Đời” là tác phẩm nổi bật của Cố Mạn, kể về mối tình sâu sắc giữa luật sư Hà Dĩ Thâm và nhiếp ảnh gia Triệu Mặc Sênh. Hai con người từng xa cách suốt bảy năm vì hiểu lầm nhưng vẫn không ngừng yêu thương và chờ đợi. Cuốn sách là biểu tượng của tình yêu bền bỉ, thủy chung giữa thời đại thực dụng.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744090827/ben-nhau-tron-doi_yeao4k.jpg', '9786048965327', 'Tiếng Việt', 420, 95000, '2022-05-05', 'NXB Văn học', 5, 14, 20, 'Bên Nhau Trọn Đời', 0x1a5bed33184811f09d2700155d851915, 0x13e9536c184811f09d2700155d851915),
(0x1f7a084c184811f09d2700155d851915, 5, '“365 Món Ngon Mỗi Ngày” là cẩm nang ẩm thực thực tế cho những ai yêu thích nấu ăn và chăm sóc gia đình. Cuốn sách tổng hợp các công thức nấu ăn từ món truyền thống đến hiện đại, đơn giản, dễ làm và phù hợp với khẩu vị người Việt, giúp làm mới bữa cơm mỗi ngày.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744091270/img128_5_sx3poi.jpg', '9786045830135', 'Tiếng Việt', 380, 69000, '2022-01-05', 'NXB Phụ nữ', 4, 20, 0, '365 Món Ngon Mỗi Ngày', 0x1a5bed51184811f09d2700155d851915, 0x13e95384184811f09d2700155d851915),
(0x1f7a0968184811f09d2700155d851915, 5, '“Ẩm Thực Việt Nam” đưa bạn đến hành trình khám phá những món ăn đặc sắc và truyền thống khắp ba miền đất nước. Mỗi món ăn đều mang trong mình hồn dân tộc và văn hóa vùng miền, được trình bày kèm hình ảnh và công thức rõ ràng, dễ thực hiện tại nhà.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744349651/am-thuc-viet-nam-thuoc-top-ngon-nhat-the-gioi-104749_ou0hxx.jpg', '9786045832562', 'Tiếng Việt', 320, 70000, '2021-11-15', 'NXB Thế giới', 5, 16, 27, 'Ẩm Thực Việt Nam', 0x1a5bde91184811f09d2700155d851915, 0x13e95384184811f09d2700155d851915),
(0x1f7a0a70184811f09d2700155d851915, 4.6, '“Nghệ Thuật Nấu Ăn” là một cuốn sách hướng dẫn toàn diện từ kỹ thuật cơ bản đến các món ăn cao cấp, dành cho những ai muốn nâng cao kỹ năng nấu nướng chuyên nghiệp. Nội dung được trình bày khoa học, trực quan và truyền cảm hứng cho người đọc về tình yêu với căn bếp.', 'https://simg.zalopay.com.vn/zlp-website/assets/sach_day_nau_an_ngon_Ve_Nha_An_Com_823f5ebc37.jpg', '9786045831246', 'Tiếng Việt', 420, 85000, '2022-04-25', 'NXB Phụ nữ', 5, 10, 18, 'Nghệ Thuật Nấu Ăn', 0x1a5bed73184811f09d2700155d851915, 0x13e95384184811f09d2700155d851915),
(0x1f7a0b83184811f09d2700155d851915, 4.5, '“Sống Lâu Trăm Tuổi” là một cẩm nang thiết thực về sức khỏe, đưa ra những bí quyết đơn giản, dễ áp dụng trong cuộc sống hằng ngày để tăng cường sức đề kháng, duy trì năng lượng tích cực và kéo dài tuổi thọ. Cuốn sách kết hợp kiến thức khoa học với kinh nghiệm dân gian, phù hợp với mọi đối tượng độc giả.', '/images/books/songLauTramTuoi.jpg', '9786045830425', 'Tiếng Việt', 280, 78000, '2022-03-10', 'NXB Y học', 5, 12, 25, 'Sống Lâu Trăm Tuổi', 0x1a5bed92184811f09d2700155d851915, 0x13e9539a184811f09d2700155d851915),
(0x1f7a0c87184811f09d2700155d851915, 4.6, '“Nhân Tố Enzyme” là tác phẩm nổi bật của bác sĩ Hiromi Shinya, giới thiệu quan điểm khoa học về việc sống khỏe mạnh thông qua việc ăn uống đúng cách, ưu tiên thực phẩm tự nhiên và cân bằng enzyme trong cơ thể. Cuốn sách làm thay đổi tư duy sức khỏe của hàng triệu người.', '/images/books/nhanToEnzyme.jpg', '9786045831853', 'Tiếng Việt', 350, 95000, '2021-07-25', 'NXB Thế giới', 5, 11, 16, 'Nhân Tố Enzyme', 0x1a5bed92184811f09d2700155d851915, 0x13e9539a184811f09d2700155d851915),
(0x1f7a0dc3184811f09d2700155d851915, 4.4, '“Bí Quyết Sống Khỏe” là hướng dẫn thiết thực cho lối sống lành mạnh, bao gồm chế độ ăn uống, luyện tập thể chất và quản lý căng thẳng tinh thần. Cuốn sách cung cấp kiến thức cơ bản và lời khuyên dễ áp dụng để nâng cao sức khỏe và phòng tránh bệnh tật.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748014797/Picture1_p588cn.png', '9786045830937', 'Tiếng Việt', 240, 72000, '2022-01-15', 'NXB Y học', 5, 12, 21, 'Bí Quyết Sống Khỏe', 0x1a5bde91184811f09d2700155d851915, 0x13e9539a184811f09d2700155d851915),
(0x1f7a0e9f184811f09d2700155d851915, 4.7, '“Xách Ba Lô Lên Và Đi” là hành trình truyền cảm hứng khám phá thế giới của Huyền Chip, một cô gái trẻ dám từ bỏ công việc ổn định để thực hiện ước mơ du lịch khắp nơi. Cuốn sách thể hiện tinh thần tự do, vượt qua nỗi sợ và giới hạn bản thân, lan tỏa thông điệp sống trọn vẹn với đam mê.', '/images/books/xachBaLo.jpg', '9786045830241', 'Tiếng Việt', 368, 89000, '2022-06-10', 'NXB Hội Nhà văn', 5, 11, 19, 'Xách Ba Lô Lên Và Đi', 0x1a5bedb1184811f09d2700155d851915, 0x13e953ae184811f09d2700155d851915),
(0x1f7a0f89184811f09d2700155d851915, 4.5, '“Tuổi Trẻ Trong Những Chuyến Đi” là tuyển tập những câu chuyện và trải nghiệm đáng nhớ của người trẻ Việt Nam trên các hành trình khám phá bản thân và thế giới xung quanh. Mỗi trang sách đều truyền tải thông điệp tích cực về sự dũng cảm, khát khao tự do và hành trình tìm kiếm ý nghĩa cuộc sống.', '/images/books/tuoiTreChuyenDi.jpg', '9786045831357', 'Tiếng Việt', 320, 75000, '2021-09-10', 'NXB Trẻ', 5, 18, 23, 'Tuổi Trẻ Trong Những Chuyến Đi', 0x1a5bedcf184811f09d2700155d851915, 0x13e953ae184811f09d2700155d851915),
(0x1f7a106b184811f09d2700155d851915, 4.6, '“Một Mình Ở Châu Âu” là câu chuyện du ký đặc sắc của tác giả trẻ người Việt, ghi lại những trải nghiệm chân thực và thú vị trên hành trình khám phá các quốc gia châu Âu. Cuốn sách không chỉ là nhật ký hành trình mà còn là hành trình trưởng thành, tự lập và sống hết mình với tuổi trẻ.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748014841/10854-mot-minh-o-chau-au-1_ibtr0s.jpg', '9786045832981', 'Tiếng Việt', 300, 92000, '2022-04-15', 'NXB Hội Nhà văn', 5, 12, 11, 'Một Mình Ở Châu Âu', 0x1a5beded184811f09d2700155d851915, 0x13e953ae184811f09d2700155d851915),
(0x1f7a118f184811f09d2700155d851915, 4.4, '“Giải Tích 1” là giáo trình toán học cơ bản dành cho sinh viên đại học, trình bày chi tiết về giới hạn, đạo hàm, tích phân và các ứng dụng. Với phương pháp giảng giải mạch lạc, ví dụ minh họa phong phú, sách giúp sinh viên dễ dàng tiếp cận và áp dụng trong học tập và nghiên cứu.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748014956/OIP_u06b2j.jpg', '9786045830159', 'Tiếng Việt', 320, 68000, '2022-07-20', 'NXB Giáo dục', 5, 28, 47, 'Giải Tích 1', 0x1a5bee0b184811f09d2700155d851915, 0x13e953c1184811f09d2700155d851915),
(0x1f7a1281184811f09d2700155d851915, 4.3, '“Đại Số Tuyến Tính” trình bày có hệ thống các khái niệm cơ bản của đại số tuyến tính như ma trận, định thức, không gian vector, ánh xạ tuyến tính và giá trị riêng. Đây là giáo trình cần thiết cho sinh viên các ngành khoa học tự nhiên và kỹ thuật.', '/images/books/daiSoTuyenTinh.jpg', '9786045831578', 'Tiếng Việt', 280, 72000, '2021-08-15', 'NXB Giáo dục', 5, 21, 39, 'Đại Số Tuyến Tính', 0x1a5bee29184811f09d2700155d851915, 0x13e953c1184811f09d2700155d851915),
(0x1f7a1369184811f09d2700155d851915, 4.5, '“Cơ Sở Dữ Liệu” là giáo trình chuyên ngành dành cho sinh viên công nghệ thông tin, cung cấp kiến thức toàn diện từ mô hình quan hệ, SQL, thiết kế cơ sở dữ liệu đến quản trị và tối ưu hóa truy vấn. Cuốn sách giúp người học tiếp cận bài bản và thực tiễn với lĩnh vực lưu trữ và xử lý dữ liệu.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748014974/OIP_dmptvi.jpg', '9786045832376', 'Tiếng Việt', 350, 75000, '2022-01-10', 'NXB Bách khoa Hà Nội', 5, 27, 40, 'Cơ Sở Dữ Liệu', 0x1a5bee49184811f09d2700155d851915, 0x13e953c1184811f09d2700155d851915),
(0x2aeda0b31a6b4b19b100aa45b92a21c4, 0, '“Cây chuối non đi giày xanh” là một câu chuyện đầy cảm xúc về tuổi thơ tại vùng quê Việt Nam. Tác phẩm tái hiện những ký ức trong trẻo, những trò chơi ngây thơ và tình cảm gia đình, hàng xóm đong đầy yêu thương qua cái nhìn ngây ngô nhưng sâu sắc của trẻ nhỏ.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748882122/OIP_duas8d.jpg', '9786041109347', 'Tiếng Việt', 250, 110000, '2010-07-15', 'NXB Trẻ', 3, 1, 29, 'Cây chuối non đi giày xanh', 0x54fb3e59202f40259ef1f769443fdab9, NULL),
(0x3e55d1332dc446358e0a61ce46b12fed, 0, '“Đất rừng phương Nam” là tác phẩm văn học nổi tiếng của Đoàn Giỏi, kể về cuộc sống phiêu lưu của cậu bé An trong kháng chiến chống Pháp tại vùng rừng U Minh. Cuốn sách vừa mang tính sử thi vừa đậm chất nhân văn, phản ánh sinh động thiên nhiên và con người Nam Bộ.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1747066888/3a3015d2864b61086f44fba984fcad49_xtg7re.jpg', '9786041109345', 'Tiếng Việt', 230, 85000, '2020-04-04', 'Nhà xuất bản Kim Đồng', 5, 3200, 120, 'Đất rừng phương Nam', 0x29cb2c4f19734582850365b42223a609, 0x13e9525f184811f09d2700155d851915),
(0xbda39f6332e711f082a900155d7083ad, 4.8, '“Con chó xấu xí” là một truyện dài xúc động viết về mối quan hệ gắn bó giữa con người và loài vật. Tác phẩm ca ngợi tình bạn chân thành, sự trung thành và lòng nhân hậu trong một xã hội đang thay đổi nhanh chóng, đặc biệt là ở vùng nông thôn Việt Nam.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1747562554/conchoxauxi_m2ttvy.jpg', '9786049634582', 'Tiếng Việt', 120, 68000, '2020-06-15', 'NXB Kim Đồng', 3, 350, 60, 'Con chó xấu xí', 0xaac59b3132e711f082a900155d7083ad, 0x13e9525f184811f09d2700155d851915),
(0xd10ca66dd1f648899738346b2cb1a653, 5, '“Project Hail Mary” là một tiểu thuyết khoa học viễn tưởng hấp dẫn của Andy Weir, xoay quanh hành trình đơn độc của Ryland Grace – một nhà khoa học bất đắc dĩ được giao nhiệm vụ cứu Trái Đất khỏi diệt vong bởi một sinh vật không gian ăn ánh sáng mặt trời. Trong chuyến đi không có đường lui, anh bất ngờ kết bạn với một sinh vật ngoài hành tinh thông minh, cùng nhau vượt qua những thử thách để tìm ra giải pháp sống còn cho cả hai loài.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1747081234/project_hail_mary.jpg', '978-0-593-13520-4', 'English', 496, 210000, '2021-05-04', 'Ballantine Books', 1, 3202, 58, 'Project Hail Mary', 0x3925276b245947f3946f640457e674bc, 0x13e93799184811f09d2700155d851915),
(0xffe68fa6f5c645be9917aea61f50f4d0, 0, '“Tuổi Thơ Dữ Dội” là một tiểu thuyết chiến tranh nổi tiếng của Phùng Quán, tái hiện chân thực và cảm động cuộc sống và chiến đấu của những thiếu niên anh hùng trong thời kỳ kháng chiến chống Pháp. Tác phẩm ca ngợi tinh thần quả cảm, lòng yêu nước và ý chí kiên cường vượt lên mọi hoàn cảnh khốc liệt của các em nhỏ, làm lay động hàng triệu trái tim qua nhiều thế hệ.', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748965066/tuoi-tho-du-doi-tap-1_ia7fxf.jpg', '978-604-9901234', 'Tiếng Việt', 420, 99000, '2019-09-01', 'Nhà xuất bản Kim Đồng', 7, 0, 80, 'Tuổi thơ dữ dội', 0x299980d0e78243ecab3b114836fed6d8, 0x13e9525f184811f09d2700155d851915);

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `id` binary(16) NOT NULL,
  `total_price` bigint(20) NOT NULL,
  `user_id` binary(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carts`
--

INSERT INTO `carts` (`id`, `total_price`, `user_id`) VALUES
(0x018a4bbe524e4978bb4af3af4d61960c, 0, 0xf207b218a13b4bc8aff035b41a5f2f33),
(0x224c38df53e144b9b13d791ca648017a, 189000, 0xab87e54994dd4c04883db71141eeedbc),
(0x3185276c0ff74656bb89525b6e1a36b5, 567000, 0xb83d3d4a9e6f45ee9140ffa8dea3d8bd),
(0x7ea20c355cea45acbda51d770b69eaa0, 0, 0xacd565dd760a453b844857cc1602d0ae),
(0x8ebace84d23d4b3ab9a1609bb616a804, 150000, 0x26ce5564f32a4125b8cad3c89f80466c),
(0x8f5e4c75fcb04aa6b315a1ca6fc051c6, 0, 0xcc41b62078ec4f8cb128bc21d2b8fc36),
(0x97cbd415fd4043a8a2b7c21ef3255e93, 0, 0x15e425bbeae3476e970b4afc0a1f5932),
(0xa585514bd6a0445bb99f983c65e86cf6, 0, 0x21ae05238118489a86cf7edf55d31901),
(0xa8ee3f70aad84f5fa99d9b7b20aa6232, 100000, 0x78e6465a87da45bcb30c0072453d8ce1),
(0xc11855b4e1c44681881c932590f44157, 0, 0xb231fe240f1a47dcacaaf2084002431e),
(0xc32ea05b51a945f49aee1dbcfef517ae, 0, 0x8a1a64cdcaa442b98c3d37195bba136f),
(0xc409eb7e4f564e6fa340cc10e1ad18b7, 0, 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0xd32a2ea560b4474d89b581a681df6ef7, 378000, 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0xd32d774fa5ac4b38a3737fbae0c50556, 0, 0xd17598bdd6d0482b867c38b82d35e56c),
(0xd52e75c4da334b83b593759e7aaa8c8d, 240000, 0x20fdbd3310754cc2af1ad8213978fb7f),
(0xd6fc733002944f9a9c0aba6abb37c936, 340000, 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xe22d34b4a1c2405b9baf3dbc331f0e02, 0, 0x4e916c2f24624a0eb1322ad0699a26c3),
(0xe3c9c571f3854c549a4ea677d24a1f20, 0, 0x31be4722e311440eb571c33a65c3992a),
(0xe8c63506dab04bd28e98e9613a4d70c2, 0, 0xa180561dbf4349e080760c5c466136c0);

-- --------------------------------------------------------

--
-- Table structure for table `cart_items`
--

CREATE TABLE `cart_items` (
  `id` binary(16) NOT NULL,
  `book_price` bigint(20) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `book_id` binary(16) DEFAULT NULL,
  `cart_id` binary(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cart_items`
--

INSERT INTO `cart_items` (`id`, `book_price`, `quantity`, `book_id`, `cart_id`) VALUES
(0x07caffb423ae45b9abaeb92ed4461607, 189000, 1, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0x224c38df53e144b9b13d791ca648017a),
(0x709907458c5849f38f3c037ee236b791, 189000, 3, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0x3185276c0ff74656bb89525b6e1a36b5),
(0x81c33f04854c4040b5fb43df2f2ddc63, 70000, 1, 0x1f79f6b0184811f09d2700155d851915, 0xd52e75c4da334b83b593759e7aaa8c8d),
(0xb2c80b6273da4f3693ab8d5c29208baf, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0xd52e75c4da334b83b593759e7aaa8c8d);

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` binary(16) NOT NULL,
  `category_img` varchar(255) DEFAULT NULL,
  `category_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `category_img`, `category_name`) VALUES
(0x13e93799184811f09d2700155d851915, 'https://theme.hstatic.net/200000512563/1000871060/14/categorybanner2_1_img.jpg?v=124', 'Khoa học - Công nghệ'),
(0x13e951fb184811f09d2700155d851915, 'https://theme.hstatic.net/200000512563/1000871060/14/categorybanner_4_img.jpg?v=124', 'Tâm lý - Kỹ năng sống'),
(0x13e9525f184811f09d2700155d851915, 'https://theme.hstatic.net/200000512563/1000871060/14/categorybanner_5_img.jpg?v=124', 'Văn học'),
(0x13e95276184811f09d2700155d851915, 'https://theme.hstatic.net/200000512563/1000871060/14/categorybanner2_5_img.jpg?v=124', 'Thiếu nhi'),
(0x13e9528f184811f09d2700155d851915, 'https://theme.hstatic.net/200000512563/1000871060/14/categorybanner_3_img.jpg?v=124', 'Truyện - Giả tưởng'),
(0x13e952aa184811f09d2700155d851915, 'https://theme.hstatic.net/200000512563/1000871060/14/categorybanner2_2_img.jpg?v=124', 'Kinh tế - Kinh doanh'),
(0x13e952bf184811f09d2700155d851915, 'https://theme.hstatic.net/200000512563/1000871060/14/categorybanner_1_img.jpg?v=124', 'Lịch sử - Chính trị'),
(0x13e9536c184811f09d2700155d851915, 'https://theme.hstatic.net/200000512563/1000871060/14/categorybanner2_2_img.jpg?v=124', 'Tình cảm - Lãng mạn'),
(0x13e95384184811f09d2700155d851915, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744278954/mon-an-ngon_wds93w.jpg', 'Ẩm thực - Nấu ăn'),
(0x13e9539a184811f09d2700155d851915, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1749117044/y9gx7nrkrtdzvtqsejxg.jpg', 'Sức khỏe - Đời sống'),
(0x13e953ae184811f09d2700155d851915, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744279056/check-in-9148_htkgig.jpg', 'Du ký - Du lịch'),
(0x13e953c1184811f09d2700155d851915, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1744279176/Bia_20thang_2010_20chuan_20up_20web_ohdqzw.jpg', 'Giáo khoa - Tham khảo');

-- --------------------------------------------------------

--
-- Table structure for table `delivery_type`
--

CREATE TABLE `delivery_type` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` decimal(38,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `districts`
--

CREATE TABLE `districts` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `province_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `districts`
--

INSERT INTO `districts` (`id`, `name`, `province_id`) VALUES
(1442, 'Quận 1', 202),
(1490, 'Quận Hoàng Mai', 201),
(1710, 'Huyện Thanh Trì', 201),
(1723, 'Huyện Hòa Bình', 253),
(1777, 'Huyện Hàm Thuận Bắc', 258),
(1840, 'Huyện Hải Hậu', 231),
(1924, 'Huyện Định Hóa', 244),
(1965, 'Huyện Lục Nam', 248),
(1989, 'Huyện Phong Thổ', 264),
(2045, 'Huyện Văn Giang', 268),
(2060, 'Thị xã Mường Lay', 265),
(2156, 'Huyện Lạc Sơn', 267),
(2157, 'Huyện Lạc Thủy', 267),
(2170, 'Huyện Mường Ảng', 265),
(2205, 'Huyện Sa Thầy', 259),
(2264, 'Huyện Si Ma Cai', 269),
(2270, 'Huyện Yên Thủy', 267),
(3230, 'Huyện Mường La', 266),
(3311, 'Huyện Văn Quan', 247),
(3440, 'Quận Nam Từ Liêm', 201);

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE `message` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `room_id` varchar(255) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `sent_at` datetime(6) DEFAULT NULL,
  `sender_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id` binary(16) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `is_read` bit(1) DEFAULT NULL,
  `item_count` int(11) NOT NULL,
  `redirect_url` varchar(255) DEFAULT NULL,
  `scope` enum('BUYER','SHOP') DEFAULT NULL,
  `thumbnail_url` varchar(255) DEFAULT NULL,
  `receiver_id` binary(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`id`, `content`, `created_at`, `is_read`, `item_count`, `redirect_url`, `scope`, `thumbnail_url`, `receiver_id`) VALUES
(0x03890d679bb140aca62ca7399ad232c5, 'Đơn hàng %s đã được tạo thành côngfdb81336-3e18-4f5d-8370-c4abdeffce33', '2025-04-18 16:50:33.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x04b25580409c4f7aa55fd78f346280c5, 'Đơn hàng %s đã được tạo thành công2710e7ab-24ae-4d8a-a144-a41cea0eb487', '2025-05-03 22:17:38.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x0a673f9a9edd4c8dab96821db147cfb3, 'Đơn hàng %s đã được tạo thành côngab6a6472-bcea-4810-961b-b090cc570def', '2025-04-19 10:27:02.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x0cc0fd798f1545d7bc3179a782937d60, 'Đơn hàng %s đã được tạo thành cônga841f0a4-6c1d-449d-8bbe-c98282f2ba7c', '2025-04-18 13:23:26.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x0dbc53d11fcb4f3f9ab7daca61ec4a38, 'Đơn hàng %s đã được tạo thành cônga9c5f3dd-c397-45f2-8e87-984cfb8bcd47', '2025-05-28 23:56:06.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0x0f483856ec574ae0b37289a883cedfb2, 'Đơn hàng %s đã được tạo thành côngfca48a07-1ae8-4e20-9493-ea674c06c7f4', '2025-04-20 14:32:21.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x0ff3dd8425de4efc93cc431cb1f62618, 'Đơn hàng %s đã được tạo thành côngec98168d-b59a-4fdc-9223-244e68223a52', '2025-05-05 22:18:12.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x145038f68b054d6ba94cddf19827404c, 'Đơn hàng %s đã được tạo thành công2a92c7ab-c59b-47e9-8311-c71cd171196e', '2025-05-06 14:54:38.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0x1c4dcad5670f489f94f0d029574157e5, 'Đơn hàng %s đã được tạo thành công593f7d57-bde9-4ebd-9a4a-0353a87a175b', '2025-04-21 13:15:38.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x1caa47ba4a9448ba93dca8d75fb83e00, 'Đơn hàng %s đã được tạo thành côngb53566ed-4d36-4201-b168-6bb9b1d25fc4', '2025-05-05 13:46:22.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x1d039b5744514b9e8401e33e0388ebc5, 'Đơn hàng %s đã được tạo thành côngbcc07161-b966-4b11-b96e-05c76672513b', '2025-04-21 13:20:10.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x1f25ce0e931646f4b2846eb5c2fa73a0, 'Đơn hàng %s đã được tạo thành côngb391fe30-7a53-4ded-b115-9a0d5b68e0a2', '2025-04-21 12:34:20.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x22b92fafd43f4f1997370d998c1a6f41, 'Đơn hàng %s đã được tạo thành công89542291-460f-442b-a58e-f36565eca891', '2025-04-19 17:23:14.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x22fb5f130938411c92d2036172a8829f, 'Đơn hàng %s đã được tạo thành công301446df-c1c6-493b-8415-7a0f6745d42a', '2025-04-19 17:01:16.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x24144389647a4a06b4c14244e29ba0fc, 'Đơn hàng %s đã được tạo thành công621ccd4d-8c6f-4d3d-bdec-3fe245fe49be', '2025-04-13 16:20:55.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x251f5d9dede84e2f9fa396cb0c3d9bf2, 'Đơn hàng %s đã được tạo thành côngb3ef5caa-24c2-4eb8-8f47-501ad06d3cff', '2025-04-20 14:54:27.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x284c806c6589418ba93e79a8fbff2603, 'Đơn hàng %s đã được tạo thành cônge5382057-ca28-4eb1-86bd-dd5291a5699f', '2025-04-18 16:49:12.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x28ee4b8c4fa44e54a4d4ac0beb46c78e, 'Đơn hàng %s đã được tạo thành công72b113c3-1abb-4dfe-b6d5-b814a32867e5', '2025-04-17 23:08:01.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x2a6619dfd3af4ea1b3bd994b72540c53, 'Đơn hàng %s đã được tạo thành côngb8c3cbdc-dc43-4183-9c99-e154951dd997', '2025-05-05 14:03:40.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x2ac2431bb3bc42149249d4a69afc7846, 'Đơn hàng %s đã được tạo thành côngea9d7181-b13c-4460-98af-371fb24f17b3', '2025-04-21 21:33:07.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x2b962678c6ad4458a9b20d3d0394ce63, 'Đơn hàng %s đã được tạo thành công78e39dae-ad3f-43f5-be74-4e9353e55b59', '2025-04-16 23:08:43.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x2c667b1b939246d9b4bff5af1f516f2b, 'Đơn hàng %s đã được tạo thành công3dfd9334-6ad2-4548-819d-f4a414c24799', '2025-04-19 12:14:31.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x2dfd08766dd34c84b8f1c0de0c71eb00, 'Đơn hàng %s đã được tạo thành công0d0443a9-d318-48a3-bb31-cc7d1f82bfb4', '2025-05-06 08:41:54.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0x2faf1b6baace46f3aec53982d2174aab, 'Đơn hàng %s đã được tạo thành công15bd4e77-60d6-48eb-99eb-007d5b8d1568', '2025-04-19 17:02:52.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x375ee4cf590e430eb50eeb37944b16bf, 'Đơn hàng %s đã được tạo thành công942c2199-754f-4322-a568-848dfe53355e', '2025-05-06 18:01:10.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0x3814be7d6d034d3ea228258e82507731, 'Đơn hàng %s đã được tạo thành cônga53c3618-eb35-4860-a419-0f2b7c1b0981', '2025-04-21 21:19:05.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x3964fb6c007747398dee0d009c92e6bc, 'Đơn hàng %s đã được tạo thành cônge07d4e5b-fbf5-453b-9a2c-eeab471e72e8', '2025-04-17 23:09:12.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x39a70f15a3d44c39a26b9fc8c2cab504, 'Đơn hàng %s đã được tạo thành côngb29e02ac-e4c7-4a72-b985-b6b39a3cd8de', '2025-04-22 08:34:44.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x3b3898a2a7254895b97ca7b05f07f5dd, 'Đơn hàng %s đã được tạo thành công11bbf51f-9d29-4706-afde-e5816851ae4b', '2025-05-06 18:19:51.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0x3d750746fefa4a59b25a100673af9daf, 'Đơn hàng %s đã được tạo thành côngb0102746-929b-4c63-8ff3-9a7efdd9f77d', '2025-05-06 18:16:00.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0x40605237851d425aa3c28fdfc811e47b, 'Đơn hàng %s đã được tạo thành công5fd5be86-1cda-4ceb-8899-7e4e57df133c', '2025-05-31 23:23:55.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xb83d3d4a9e6f45ee9140ffa8dea3d8bd),
(0x40e1b6d9d5804374901f60f23beae9d6, 'Đơn hàng %s đã được tạo thành côngf312ddc8-cf26-4348-8fa6-b2b31d76c9a8', '2025-04-18 23:41:24.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x4425710c96874277842ce0bb4d385804, 'Đơn hàng %s đã được tạo thành công6ea24204-1903-4d85-8539-63cf3a35ff36', '2025-04-17 01:24:42.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x455079605a0a480eb41db5d25652bf2f, 'Đơn hàng %s đã được tạo thành công12aaedb9-a748-4b48-997e-b6bfc259bbef', '2025-05-28 23:52:12.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0x49e5ef82fe6b4b12b2271d53fcf556f8, 'Đơn hàng %s đã được tạo thành côngeb0dc3b0-a931-4320-89ed-ac13835bfbb1', '2025-05-05 22:12:26.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x4bf926bad8674148806593230d3aacbd, 'Đơn hàng %s đã được tạo thành công9b4cd1d4-5458-460e-8d9f-9468ac427917', '2025-04-16 23:05:37.000000', b'0', 6, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x4e721007d5404fcf93ba091d66211850, 'Đơn hàng %s đã được tạo thành côngae668ecf-9b86-471e-97f2-aa240c265fad', '2025-04-21 12:51:42.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x4fd658b13bd94daf8bd844a4adf0e763, 'Đơn hàng %s đã được tạo thành công62ea0eeb-d8f8-45ca-9635-d0b08a0ed00e', '2025-04-13 16:28:37.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x5050a5334e484eaab476778a0700c496, 'Đơn hàng %s đã được tạo thành cônge38652d1-f3da-4629-b854-a80d352a13ca', '2025-05-03 22:24:42.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x5144640886014d59adadc9f9efa0f46a, 'Đơn hàng %s đã được tạo thành côngb0bc7465-a0fc-44c1-b66d-4dd6810d03ba', '2025-05-06 11:17:04.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0x524349a880164204a56cf9e967a7bbd0, 'Đơn hàng %s đã được tạo thành công08821028-ab70-4661-a750-a4f844842c43', '2025-04-20 21:43:09.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x53968e4933db4e908c9800548fbbab3d, 'Đơn hàng %s đã được tạo thành cônged68f8f8-364a-4536-aadb-6f6a84cd7f83', '2025-04-18 15:51:07.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x55d55de3b72c4e5ab2e19663dd8ce32a, 'Đơn hàng %s đã được tạo thành côngb22df640-ca63-4ae9-8416-8faa40011829', '2025-04-21 21:13:05.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x57af431aff8643df99efedde92068eec, 'Đơn hàng %s đã được tạo thành công6a8666a4-f149-4a56-ae51-08b8fbf1e9d9', '2025-05-05 21:45:32.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x5a099af388034604a8cf5d854b35cda5, 'Đơn hàng %s đã được tạo thành công6038c459-cb27-4a3a-adcc-1fed6ae32f98', '2025-05-20 11:17:22.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x5b912ec1246f48039b477bbbca7a43d7, 'Đơn hàng %s đã được tạo thành công97487f90-ccba-40ac-8262-879c15c49a34', '2025-04-21 12:08:29.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x5f890e1d50e24516a5527857d7f3dc2b, 'Đơn hàng %s đã được tạo thành côngbb537a27-b54a-4c51-bfbd-756884365a93', '2025-06-08 22:40:04.000000', b'0', 4, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0x5f9b48fbe8434b68bd725d6ab3a86d24, 'Đơn hàng %s đã được tạo thành công4b11f1dd-5763-4aa9-863e-9653aeccd35f', '2025-04-17 22:20:57.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x5fef3e7239b84e01be4f2b3818ac87e5, 'Đơn hàng %s đã được tạo thành côngd569c5cd-5d8d-4f45-a2fb-6bf05ff1ff72', '2025-05-20 20:31:06.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x611817b12a204fd9b60b342dfb5df666, 'Đơn hàng %s đã được tạo thành côngd7661704-f0ed-4f26-862a-70f455065a3f', '2025-04-22 09:33:19.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x618ad042d04e4d0a88613b02be19b57f, 'Đơn hàng %s đã được tạo thành công13a88ab2-8018-4fd7-8438-3d973df33bf6', '2025-04-20 21:35:24.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x62ffea7544a94b8ba7e1412fc5ebbfd1, 'Đơn hàng %s đã được tạo thành công8ffcc557-47cb-4c7b-b045-940495418567', '2025-05-06 14:49:25.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0x6530b4d0f76b46e9afcdf9bcce01d9b1, 'Đơn hàng %s đã được tạo thành công1b9f6586-5f5e-42a6-b990-5b526f2879c6', '2025-05-05 22:02:00.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x6583656a44154e61acf5b465507ff0b3, 'Đơn hàng %s đã được tạo thành côngf64d27e9-fa9b-44a4-9c26-3b3574b60397', '2025-05-28 18:04:05.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xa180561dbf4349e080760c5c466136c0),
(0x6bca004f169f4edbb4d863554ee6660c, 'Đơn hàng %s đã được tạo thành côngd7e6925e-b1a3-4b15-81f1-bfcc3a39c4e0', '2025-05-03 22:37:24.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x6cc9933901d24afa8fe233828e64f905, 'Đơn hàng %s đã được tạo thành công1376c16a-5476-47be-9489-5b5127fc2d8e', '2025-05-06 14:56:53.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0x6ced2d09de094a7a90ad807b47ce4e69, 'Đơn hàng %s đã được tạo thành côngd97d27cf-bf5e-4397-be2f-d9869b2ea5ba', '2025-04-21 11:58:51.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x6fee11bb092446a0a3c4b7084ebfd1cc, 'Đơn hàng %s đã được tạo thành công6633d15a-94ed-4edb-a6f2-df2a80d1691c', '2025-05-05 22:09:37.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x70c2b3304f32466b8aa6c73ae6f93add, 'Đơn hàng %s đã được tạo thành công0cf244cb-606c-4e3e-9c61-37bdff1f4fe1', '2025-05-25 23:08:19.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x715d2879aa9443a18f158b46e1600474, 'Đơn hàng %s đã được tạo thành công8f836040-90ee-45b4-92e5-2ba8fb02e41a', '2025-06-02 21:32:24.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x26ce5564f32a4125b8cad3c89f80466c),
(0x71c3b7268e844cddaad6fb293c690183, 'Đơn hàng %s đã được tạo thành công59516b29-5c16-4331-9743-b8a3813062e2', '2025-05-28 17:54:09.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xa180561dbf4349e080760c5c466136c0),
(0x71d1e1eb4a7b4a25ac28204a058cdd0c, 'Đơn hàng %s đã được tạo thành công0953e559-b596-443b-9a7f-312787659ce9', '2025-05-28 18:06:37.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xa180561dbf4349e080760c5c466136c0),
(0x71da4acf1e1d4136a5138538796f55a4, 'Đơn hàng %s đã được tạo thành công8d346152-6585-49c2-b8af-4796f1de696c', '2025-05-24 22:07:19.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x73efd260170c46ae85b16d63882abae9, 'Đơn hàng %s đã được tạo thành công87be39a1-d3d5-4416-ac54-dd57c97180b0', '2025-04-20 21:25:24.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x782b74ef6e984bf79c9076947833cd1c, 'Đơn hàng %s đã được tạo thành côngdd97a2ce-d1f3-41fd-8a24-d1497529a25b', '2025-04-20 15:03:36.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x7b6ba8f99a4d4b4d8ffafca9ec6f9ae1, 'Đơn hàng %s đã được tạo thành công6b8e0786-41d3-4c21-b708-b996d5ce5cb6', '2025-04-20 14:32:40.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x7c3d9ef466434483b0ed128417d54589, 'Đơn hàng %s đã được tạo thành công94b18192-e0fd-4e0f-a0db-560c96184219', '2025-04-17 23:03:17.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x7e976dabaf1e45b886ce6827be0beb5e, 'Đơn hàng %s đã được tạo thành công37aaa826-cb2b-4d27-9eb3-c7e535727ae0', '2025-04-20 14:44:11.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x8019ced63ee241b6a6bfb7f6fc75f934, 'Đơn hàng %s đã được tạo thành côngaaf09ef0-61ef-40de-b7b5-ec2c63ee0eab', '2025-04-17 18:21:20.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x81ed4b199a2144e78477b459d3bd9e92, 'Đơn hàng %s đã được tạo thành côngbaa0c3d8-4ba7-47b2-9b6a-49a5c7863ca9', '2025-04-20 14:46:47.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x83442d68cc38486cb70fc01b7210b176, 'Đơn hàng %s đã được tạo thành công8498e8c5-2947-4e72-b442-6b4a04935af6', '2025-05-04 11:14:29.000000', b'0', 4, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x869021d3691149449e1638d3ec567c5c, 'Đơn hàng %s đã được tạo thành công514f1d08-e04f-4fae-a446-8d515fb387ad', '2025-04-17 18:18:13.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x86c0e6f9ce29485bb9c21f5c17de872a, 'Đơn hàng %s đã được tạo thành côngec4cbc62-bc48-4b22-b353-d8924af4f6d7', '2025-05-06 18:11:17.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0x88f798242e434c628b17c11a9e40e210, 'Đơn hàng %s đã được tạo thành côngf25fe019-3f5e-42b1-9b14-836a20e93f3d', '2025-04-17 22:56:41.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x8957f51bc58f49fb8aa5826473048788, 'Đơn hàng %s đã được tạo thành công21f6b5f9-3bbd-41e4-ae44-72b8c4a480ed', '2025-04-13 16:22:26.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x89c69c0d1042457e87401cc91e875cc2, 'Đơn hàng %s đã được tạo thành côngf7dc5ebb-3e80-4566-9ea4-d89884dc7a34', '2025-04-20 15:07:03.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x8dc1cea57d57464ea666db70405f97f9, 'Đơn hàng %s đã được tạo thành công81383036-8c78-4a7b-a668-9c3d845d0610', '2025-04-19 10:59:45.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x8e230c9501b54d93928d199e7e2d1449, 'Đơn hàng %s đã được tạo thành công8182a042-e163-49a4-ab68-db0120b6373f', '2025-05-28 22:34:56.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0x8f9b988da4a24217879b720dfd4a8360, 'Đơn hàng %s đã được tạo thành côngac17c3fd-36a2-484e-a780-16c7b2af0ae0', '2025-04-21 12:54:17.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x91720845434c411f9a63779fafb941bc, 'Đơn hàng %s đã được tạo thành công79ee0311-8dd9-41e1-b367-5cf12268a7f8', '2025-04-18 13:03:36.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x91dcfd22e08f468182b10e86bebf34a4, 'Đơn hàng %s đã được tạo thành công1d88f716-b612-4096-9491-b62287900404', '2025-05-28 23:47:56.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0x95252c43799c424f93da034c634a898e, 'Đơn hàng %s đã được tạo thành công2b5c50a7-c156-49ce-8f71-89c8160cae60', '2025-05-05 21:48:08.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x955dbc76fc9541089e52000dcf84bdef, 'Đơn hàng %s đã được tạo thành côngd8edfcbf-dc3a-42b4-a616-b0e5ede1376a', '2025-04-20 21:02:52.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0x96ccef7ddc3748088666eb2ce2d2fecc, 'Đơn hàng %s đã được tạo thành công9ee5f30c-d00d-40ae-b6e7-1bd251c6b758', '2025-05-05 21:54:03.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0x993f6dda10a5461fa39b100d4268afb7, 'Đơn hàng %s đã được tạo thành côngfa20cff8-1da9-4464-8105-e3e429843bad', '2025-05-30 10:24:01.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0x99aea82ac1934b7abf3cc6cf10c2e9b5, 'Đơn hàng %s đã được tạo thành côngfe0987a2-f270-444f-bad1-ecdf6f3546cd', '2025-05-06 08:28:03.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0xa0c012925bcf4296b8529f203115b2f4, 'Đơn hàng %s đã được tạo thành công56da5a94-93f1-4237-a9ff-57e7e3ed901d', '2025-04-17 22:47:05.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xa256c15cfadf4b3ab6bb9fca7a7b77d5, 'Đơn hàng %s đã được tạo thành côngbe5ba058-91b4-4799-b4fc-f945306f47c1', '2025-04-19 17:28:47.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0xa68233ad49624b3e8e779308166616f9, 'Đơn hàng %s đã được tạo thành công486707cc-b6dd-489b-a2a7-61ae9a5cbda0', '2025-04-18 16:43:47.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xa6ac8ca0d62d4a9b80b7255459b4b31b, 'Đơn hàng %s đã được tạo thành công4ce1b259-0161-4820-997c-ff3b1c5e65eb', '2025-05-28 23:53:12.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0xa8c31dedd4e7480cb9f13d2f1a69609e, 'Đơn hàng %s đã được tạo thành công7082e0db-c81d-4d6d-b4c7-f8ba15487c3d', '2025-04-17 22:37:30.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xa98826396d0c43b39c283a8e2da16b16, 'Đơn hàng %s đã được tạo thành công57352187-b2a8-467c-889b-dd342aa11d6a', '2025-05-27 22:21:09.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xa180561dbf4349e080760c5c466136c0),
(0xaceb0f6dee7d4d1b8e69fb82f6a65c98, 'Đơn hàng %s đã được tạo thành công2e534b23-c9c0-4a7b-964a-8ef1f7f18e65', '2025-04-17 22:53:10.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xb04b237864154629879428979bce038b, 'Đơn hàng %s đã được tạo thành côngb8cbf2fd-f8c6-4cec-b7f4-f85a948a5b4c', '2025-04-21 13:24:38.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0xb2da79ee4fc7407b940beb0b04fae578, 'Đơn hàng %s đã được tạo thành công17604455-fa43-4f8d-bcac-2ccdaaff9783', '2025-04-17 21:49:56.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xb7321cac37174bf3bd8c59d38a9e077d, 'Đơn hàng %s đã được tạo thành cônge1dee520-fe22-40ce-9930-e90309975076', '2025-05-05 21:05:56.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xb987fad3b06342048111ea80448c7df7, 'Đơn hàng %s đã được tạo thành công7d76b259-84d2-4fac-97ee-65af9d9252d0', '2025-05-06 08:44:31.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0xbb6de773824c4e01bb7de938a25ed090, 'Đơn hàng %s đã được tạo thành công991c622a-50e7-4bcc-9a5e-149ae74ac370', '2025-05-05 15:24:10.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x78e6465a87da45bcb30c0072453d8ce1),
(0xbc2b140e0a2a4e35afc111885c70761f, 'Đơn hàng %s đã được tạo thành côngdcbe2ff5-7a2e-4ae7-bea0-d1e5102345ff', '2025-04-17 22:31:48.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0xbd0ced7b55204bcaa63b255a07b6e14d, 'Đơn hàng %s đã được tạo thành công929fb3cb-7f8a-4ad9-8aa1-552bb036814d', '2025-04-21 21:15:06.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0xc3cea42fe83f47b7a64cc882fc024fe4, 'Đơn hàng %s đã được tạo thành công4015365d-9c72-473e-9d18-557c4b187050', '2025-04-21 21:37:20.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0xc5bf25e97cb04c90b56877ac6a8d8884, 'Đơn hàng %s đã được tạo thành công07360659-44bd-492a-847d-408417fb4b58', '2025-05-06 18:08:29.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0xcb9560b550414ab6b5280b837fa84bfd, 'Đơn hàng %s đã được tạo thành công433bb097-046d-4856-aef8-f0cf85be84ea', '2025-05-03 22:40:39.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xd0966102ab0147eeb1c8deedcf4d19af, 'Đơn hàng %s đã được tạo thành côngc86db18a-4897-467b-abcc-2a2681ab0888', '2025-04-20 14:50:33.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0xd16f45b844f24a2c9d0b7b3f367e5309, 'Đơn hàng %s đã được tạo thành công633fcdd7-9921-4dd7-932a-263ea4906b80', '2025-05-06 18:10:00.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0xd1b0ac9426634a6ebdccf463f98b112a, 'Đơn hàng %s đã được tạo thành công7bd3f5eb-03d7-47ef-9fb7-1a6b2d252626', '2025-05-05 21:35:58.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xd88e6593fae942828dbc672fa3747cce, 'Đơn hàng %s đã được tạo thành côngcf0946d2-469e-4a29-8393-798c61c0979d', '2025-04-18 13:12:01.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xdbfbe75f330c429495f891a2662c5f28, 'Đơn hàng %s đã được tạo thành côngd87103bb-59fb-437c-bbe1-c33e43fdb2b3', '2025-05-28 23:54:52.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0xe78b980c8d8440d7b40e633f0f0ddb4d, 'Đơn hàng %s đã được tạo thành côngfdfb9ba4-5c5a-4a68-8661-52a0262aa582', '2025-04-18 13:40:07.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xe9556d97b95e44fb85dcf891eff59e6e, 'Đơn hàng %s đã được tạo thành côngeb1e5e51-4be5-436c-9a2c-0f6ec9431c0c', '2025-05-06 08:39:40.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0xeb20a06c13af48a389d3eb7234b9b3c3, 'Đơn hàng %s đã được tạo thành côngc8a81498-469f-42fc-8b6d-4311bac2216c', '2025-04-17 22:39:53.000000', b'0', 3, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xeece1f42db444897805321a3e3f1855b, 'Đơn hàng %s đã được tạo thành côngcbfdf99d-47a7-44a0-95cb-00b8a845c340', '2025-04-21 12:39:11.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0xef39fade6a414a6987d4c57baa4260be, 'Đơn hàng %s đã được tạo thành côngba238f8f-d6fa-41bb-9c40-be34af7a54cf', '2025-05-27 22:03:35.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xa180561dbf4349e080760c5c466136c0),
(0xf7a4459a4f1b4bb4829a6a0e745cec84, 'Đơn hàng %s đã được tạo thành công5fe2969c-8a80-4aa1-b942-cee59d9cac2f', '2025-04-18 13:08:27.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x7aea20848d0841ddb3575d9f242c3b9f),
(0xf9f888f26775485595a55c8c13b898a0, 'Đơn hàng %s đã được tạo thành cônge908f84b-bb43-4564-a62d-5506f59fe257', '2025-05-27 22:23:37.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xa180561dbf4349e080760c5c466136c0),
(0xfcf4cf2fc90d41dca15f1c124198ece4, 'Đơn hàng %s đã được tạo thành công21fe26ec-e06a-4747-b5c6-45b2d636d1ff', '2025-04-21 12:03:17.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1),
(0xfd187401e4ae4c9a84c54d31d46b4ee7, 'Đơn hàng %s đã được tạo thành công48f79248-d52f-4f9c-9a50-baa29baa373d', '2025-05-06 08:46:49.000000', b'0', 2, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0x4e916c2f24624a0eb1322ad0699a26c3),
(0xfe2e347e430a4728b27d5a700cb6f5ce, 'Đơn hàng %s đã được tạo thành công39a6b981-f2a8-4647-9901-c850a90c531e', '2025-04-20 14:48:48.000000', b'0', 1, NULL, 'SHOP', 'https://res.cloudinary.com/daxt0vwoc/image/upload/v1742230894/png-transparent-red-bell-notification-thumbnail_mvxxqa.png', 0xf10bf57d57384d0f94bf3e0b91bd99d1);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` binary(16) NOT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `payment_id` bigint(20) DEFAULT NULL,
  `user_id` binary(16) DEFAULT NULL,
  `address_id` binary(16) DEFAULT NULL,
  `estimated_delivery_date` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `create_at`, `payment_id`, `user_id`, `address_id`, `estimated_delivery_date`) VALUES
(0x0736065944bd492a847d408417fb4b58, '2025-05-06 18:08:29.000000', 94, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-08 23:59:59.000000'),
(0x08821028ab704661a750a4f844842c43, '2025-04-20 21:43:09.000000', 49, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-22 23:59:59.000000'),
(0x0953e559b596443b9a7f312787659ce9, '2025-05-28 18:06:37.000000', 108, 0xa180561dbf4349e080760c5c466136c0, 0xf1d838356643402c801c4a757502e799, '2025-05-30 23:59:59.000000'),
(0x0cf244cb606c4e3e9c6137bdff1f4fe1, '2025-05-25 23:08:19.000000', 102, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-27 23:59:59.000000'),
(0x0d0443a9d31848a3bb31cc7d1f82bfb4, '2025-05-06 08:41:54.000000', 86, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-07 23:59:59.000000'),
(0x11bbf51f9d294706afdee5816851ae4b, '2025-05-06 18:19:51.000000', 98, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-08 23:59:59.000000'),
(0x12aaedb9a7484b48997eb6bfc259bbef, '2025-05-28 23:52:12.000000', 111, 0xb653efc8f9fe4ca880bcbc63ae82418b, 0x19f7bb51280f440b91e8e451679f07f4, '2025-05-30 23:59:59.000000'),
(0x1376c16a547647be94895b5127fc2d8e, '2025-05-06 14:56:52.000000', 92, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-07 23:59:59.000000'),
(0x13a88ab280184fd784383d973df33bf6, '2025-04-20 21:35:24.000000', 48, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-22 23:59:59.000000'),
(0x15bd4e7760d648eb99eb007d5b8d1568, '2025-04-19 17:02:52.000000', 34, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-20 23:59:59.000000'),
(0x17604455fa434f8dbcac2ccdaaff9783, '2025-04-17 21:49:55.000000', 9, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x1b9f65865f5e42a6b9905b526f2879c6, '2025-05-05 22:02:00.000000', 80, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-07 23:59:59.000000'),
(0x1d88f716b61240969491b62287900404, '2025-05-28 23:47:56.000000', 110, 0xb653efc8f9fe4ca880bcbc63ae82418b, 0x19f7bb51280f440b91e8e451679f07f4, '2025-05-30 23:59:59.000000'),
(0x21f6b5f93bbd41e4ae4472b8c4a480ed, '2025-04-13 16:22:26.000000', 2, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x21fe26ece06a4747b5c645b2d636d1ff, '2025-04-21 12:03:17.000000', 51, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-25 23:59:59.000000'),
(0x2710e7ab24ae4d8aa144a41cea0eb487, '2025-05-03 22:17:38.000000', 67, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x2481decaca82455b8d5e3e49a8e64e68, '2025-05-07 23:59:59.000000'),
(0x2a92c7abc59b47e98311c71cd171196e, '2025-05-06 14:54:38.000000', 91, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-07 23:59:59.000000'),
(0x2b5c50a7c15649ce8f7189c8160cae60, '2025-05-05 21:48:07.000000', 78, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-07 23:59:59.000000'),
(0x2e534b23c9c04a7b964a8ef1f7f18e65, '2025-04-17 22:53:10.000000', 15, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x301446dfc1c6493b84157a0f6745d42a, '2025-04-19 17:01:14.000000', 33, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-20 23:59:59.000000'),
(0x37aaa826cb2b4d279eb3c7e535727ae0, '2025-04-20 14:44:11.000000', 39, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-24 23:59:59.000000'),
(0x39a6b981f2a846479901c850a90c531e, '2025-04-20 14:48:48.000000', 41, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-24 23:59:59.000000'),
(0x3dfd93346ad24548819df4a414c24799, '2025-04-19 12:14:31.000000', 32, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x0ed99e635e25445e96ce2ead532d2e6c, '2025-04-20 23:59:59.000000'),
(0x4015365d9c72473e9d18557c4b187050, '2025-04-21 21:37:20.000000', 64, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-23 23:59:59.000000'),
(0x433bb097046d4856aef8f0cf85be84ea, '2025-05-03 22:40:39.000000', 70, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, '2025-05-05 23:59:59.000000'),
(0x466fd5c1e75d4184b5242a53775bff2d, '2025-06-01 23:10:08.000000', 118, 0xb83d3d4a9e6f45ee9140ffa8dea3d8bd, 0xe718495563b5452eb3bd3b1a590854c4, '2025-06-03 23:59:59.000000'),
(0x486707ccb6dd489ba2a761ae9a5cbda0, '2025-04-18 16:43:47.000000', 26, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xaaf0d39fd48544a5848ed739c59efcf8, NULL),
(0x48f79248d52f4f9c9a50baa29baa373d, '2025-05-06 08:46:49.000000', 88, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-07 23:59:59.000000'),
(0x4b11f1dd57634aa9863e9653aeccd35f, '2025-04-17 22:20:57.000000', 10, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x4ce1b25901614820997cff3b1c5e65eb, '2025-05-28 23:53:12.000000', 112, 0xb653efc8f9fe4ca880bcbc63ae82418b, 0x19f7bb51280f440b91e8e451679f07f4, '2025-05-30 23:59:59.000000'),
(0x514f1d08e04f4faea4468d515fb387ad, '2025-04-17 18:18:13.000000', 7, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x56da5a9493f14237a9ff57e7e3ed901d, '2025-04-17 22:47:05.000000', 14, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x57352187b2a8467c889bdd342aa11d6a, '2025-05-27 22:21:09.000000', 104, 0xa180561dbf4349e080760c5c466136c0, 0xf1d838356643402c801c4a757502e799, '2025-05-29 23:59:59.000000'),
(0x593f7d57bde94ebd9a4a0353a87a175b, '2025-04-21 13:15:38.000000', 57, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-22 23:59:59.000000'),
(0x59516b295c1643319743b8a3813062e2, '2025-05-28 17:54:08.000000', 106, 0xa180561dbf4349e080760c5c466136c0, 0xf1d838356643402c801c4a757502e799, '2025-05-29 23:59:59.000000'),
(0x5fd5be861cda4ceb88997e4e57df133c, '2025-05-31 23:23:55.000000', 116, 0xb83d3d4a9e6f45ee9140ffa8dea3d8bd, 0xe718495563b5452eb3bd3b1a590854c4, '2025-06-02 23:59:59.000000'),
(0x5fe2969c8a804aa1b942cee59d9cac2f, '2025-04-18 13:08:27.000000', 21, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x2481decaca82455b8d5e3e49a8e64e68, NULL),
(0x6038c459cb274a3aadcc1fed6ae32f98, '2025-05-20 11:17:22.000000', 99, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x7153fdb1e52c45929fd307464e8627eb, '2025-05-21 23:59:59.000000'),
(0x621ccd4d8c6f4d3dbdec3fe245fe49be, '2025-04-13 16:20:55.000000', 1, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x62ea0eebd8f845ca9635d0b08a0ed00e, '2025-04-13 16:28:36.000000', 3, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x633fcdd799214dd7932a263ea4906b80, '2025-05-06 18:10:00.000000', 95, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-08 23:59:59.000000'),
(0x6633d15a94ed4edba6f2df2a80d1691c, '2025-05-05 22:09:36.000000', 81, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-07 23:59:59.000000'),
(0x6a8666a4f1494a56ae5108b8fbf1e9d9, '2025-05-05 21:45:32.000000', 77, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-07 23:59:59.000000'),
(0x6b8e078641d34c21b708b996d5ce5cb6, '2025-04-20 14:32:40.000000', 38, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-24 23:59:59.000000'),
(0x6ea2420419034d85853963cf3a35ff36, '2025-04-17 01:24:42.000000', 6, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x7082e0dbc81d4d6db4c7f8ba15487c3d, '2025-04-17 22:37:28.000000', 12, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x72b113c31abb4dfeb6d5b814a32867e5, '2025-04-17 23:08:01.000000', 18, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x24baed2d054e449cb999aaf9a294bf80, NULL),
(0x78e39daead3f43f5be744e9353e55b59, '2025-04-16 23:08:43.000000', 5, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x79ee03118dd941e1b3675cf12268a7f8, '2025-04-18 13:03:36.000000', 20, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x7bd3f5eb03d747ef9fb71a6b2d252626, '2025-05-05 21:35:58.000000', 76, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-07 23:59:59.000000'),
(0x7d76b25984d24fac97ee65af9d9252d0, '2025-05-06 08:44:31.000000', 87, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-07 23:59:59.000000'),
(0x813830368c784a7ba6689c3d845d0610, '2025-04-19 10:59:45.000000', 31, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x0ed99e635e25445e96ce2ead532d2e6c, '2025-04-20 23:59:59.000000'),
(0x8182a042e16349a4ab68db0120b6373f, '2025-05-28 22:34:56.000000', 109, 0xb653efc8f9fe4ca880bcbc63ae82418b, 0x19f7bb51280f440b91e8e451679f07f4, '2025-05-30 23:59:59.000000'),
(0x8498e8c529474e72b4426b4a04935af6, '2025-05-04 11:14:29.000000', 71, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x7153fdb1e52c45929fd307464e8627eb, '2025-05-05 23:59:59.000000'),
(0x87be39a1d3d54416ac54dd57c97180b0, '2025-04-20 21:25:24.000000', 47, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-22 23:59:59.000000'),
(0x89542291460f442ba58ef36565eca891, '2025-04-19 17:23:13.000000', 35, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-20 23:59:59.000000'),
(0x8d346152658549c2b8af4796f1de696c, '2025-05-24 22:07:19.000000', 101, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-26 23:59:59.000000'),
(0x8f83604090ee45b492e52ba8fb02e41a, '2025-06-02 21:32:24.000000', 120, 0x26ce5564f32a4125b8cad3c89f80466c, 0x0dacc2fc8d944f98a45f9d79ff11910c, '2025-06-03 23:59:59.000000'),
(0x8ffcc55747cb4c7bb045940495418567, '2025-05-06 14:49:25.000000', 90, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-07 23:59:59.000000'),
(0x929fb3cb7f8a4ad98aa1552bb036814d, '2025-04-21 21:15:06.000000', 61, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-23 23:59:59.000000'),
(0x942c2199754f4322a568848dfe53355e, '2025-05-06 18:01:10.000000', 93, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-08 23:59:59.000000'),
(0x94b18192e0fd4e0fa0db560c96184219, '2025-04-17 23:03:17.000000', 17, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x97487f90ccba40ac8262879c15c49a34, '2025-04-21 12:08:28.000000', 52, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-25 23:59:59.000000'),
(0x991c622a50e74bcc9a5e149ae74ac370, '2025-05-05 15:24:10.000000', 74, 0x78e6465a87da45bcb30c0072453d8ce1, 0x50ecb90c2078429497dd2ddf9cf56f6e, '2025-05-06 23:59:59.000000'),
(0x9b4cd1d45458460e8d9f9468ac427917, '2025-04-16 23:05:36.000000', 4, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0x9ee5f30cd00d40aeb6e71bd251c6b758, '2025-05-05 21:54:03.000000', 79, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-07 23:59:59.000000'),
(0xa53c3618eb354860a4190f2b7c1b0981, '2025-04-21 21:19:04.000000', 62, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-23 23:59:59.000000'),
(0xa841f0a46c1d449d8bbec98282f2ba7c, '2025-04-18 13:23:26.000000', 23, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0xa9c5f3ddc39745f28e87984cfb8bcd47, '2025-05-28 23:56:06.000000', 114, 0xb653efc8f9fe4ca880bcbc63ae82418b, 0x19f7bb51280f440b91e8e451679f07f4, '2025-05-30 23:59:59.000000'),
(0xaaf09ef061ef40deb7b5ec2c63ee0eab, '2025-04-17 18:21:20.000000', 8, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0xab6a6472bcea4810961bb090cc570def, '2025-04-19 10:27:02.000000', 30, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xaa1302104d09489ea2c6b26cb13269c9, '2025-04-20 23:59:59.000000'),
(0xac17c3fd36a2484ea78016c7b2af0ae0, '2025-04-21 12:54:16.000000', 56, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-22 23:59:59.000000'),
(0xae668ecf9b86471e97f2aa240c265fad, '2025-04-21 12:51:42.000000', 55, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-22 23:59:59.000000'),
(0xb0102746929b4c638ff39a7efdd9f77d, '2025-05-06 18:16:00.000000', 97, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-08 23:59:59.000000'),
(0xb0bc7465a0fc44c1b66d4dd6810d03ba, '2025-05-06 11:17:04.000000', 89, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-07 23:59:59.000000'),
(0xb22df640ca634ae984168faa40011829, '2025-04-21 21:13:04.000000', 60, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-23 23:59:59.000000'),
(0xb29e02ace4c74a72b985b6b39a3cd8de, '2025-04-22 08:34:44.000000', 65, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x0ed99e635e25445e96ce2ead532d2e6c, '2025-04-23 23:59:59.000000'),
(0xb391fe307a534dedb1159a0d5b68e0a2, '2025-04-21 12:34:20.000000', 53, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-22 23:59:59.000000'),
(0xb3ef5caa24c24eb88f47501ad06d3cff, '2025-04-20 14:54:27.000000', 43, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-24 23:59:59.000000'),
(0xb53566ed4d364201b1686bb9b1d25fc4, '2025-05-05 13:46:21.000000', 72, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-06 23:59:59.000000'),
(0xb8c3cbdcdc4341839c99e154951dd997, '2025-05-05 14:03:40.000000', 73, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-06 23:59:59.000000'),
(0xb8cbf2fdf8c64cecb7f4f85a948a5b4c, '2025-04-21 13:24:38.000000', 59, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-22 23:59:59.000000'),
(0xba238f8fd6fa41bb9c40be34af7a54cf, '2025-05-27 22:03:34.000000', 103, 0xa180561dbf4349e080760c5c466136c0, 0xf1d838356643402c801c4a757502e799, '2025-05-29 23:59:59.000000'),
(0xbaa0c3d84ba747b29b6a49a5c7863ca9, '2025-04-20 14:46:47.000000', 40, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-24 23:59:59.000000'),
(0xbb537a27b54a4c51bfbd756884365a93, '2025-06-08 22:40:04.000000', 121, 0xb653efc8f9fe4ca880bcbc63ae82418b, 0x19f7bb51280f440b91e8e451679f07f4, '2025-06-10 23:59:59.000000'),
(0xbcc07161b9664b11b96e05c76672513b, '2025-04-21 13:20:10.000000', 58, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-22 23:59:59.000000'),
(0xbe5ba05891b44799b4fcf945306f47c1, '2025-04-19 17:28:46.000000', 36, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-20 23:59:59.000000'),
(0xc86db18a4897467babcc2a2681ab0888, '2025-04-20 14:50:33.000000', 42, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-24 23:59:59.000000'),
(0xc8a81498469f42fc8b6d4311bac2216c, '2025-04-17 22:39:53.000000', 13, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0xcbfdf99d47a744a095cb00b8a845c340, '2025-04-21 12:39:11.000000', 54, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-22 23:59:59.000000'),
(0xcf0946d2469e4a298393798c61c0979d, '2025-04-18 13:12:01.000000', 22, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4651bc1b184811f09d2700155d851915, NULL),
(0xd569c5cd5d8d4f45a2fb6bf05ff1ff72, '2025-05-20 20:31:06.000000', 100, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-22 23:59:59.000000'),
(0xd7661704f0ed4f26862a70f455065a3f, '2025-04-22 09:33:18.000000', 66, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x50fde11137f84f3ebf65964da2e4864b, '2025-04-22 23:59:59.000000'),
(0xd7e6925eb1a34b1581f1bfcc3a39c4e0, '2025-05-03 22:37:24.000000', 69, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, '2025-05-05 23:59:59.000000'),
(0xd87103bb59fb437cbbe1c33e43fdb2b3, '2025-05-28 23:54:52.000000', 113, 0xb653efc8f9fe4ca880bcbc63ae82418b, 0x19f7bb51280f440b91e8e451679f07f4, '2025-05-30 23:59:59.000000'),
(0xd8948e00f3924033abad11876edfa269, '2025-06-01 21:52:28.000000', 117, 0xb653efc8f9fe4ca880bcbc63ae82418b, 0x19f7bb51280f440b91e8e451679f07f4, '2025-06-03 23:59:59.000000'),
(0xd8edfcbfdc3a42b4a616b0e5ede1376a, '2025-04-20 21:02:52.000000', 46, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-22 23:59:59.000000'),
(0xd97d27cfbf5e4397be2fd9869b2ea5ba, '2025-04-21 11:58:51.000000', 50, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-25 23:59:59.000000'),
(0xdcbe2ff57a2e4ae7bea0d1e5102345ff, '2025-04-17 22:31:48.000000', 11, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x0ed99e635e25445e96ce2ead532d2e6c, NULL),
(0xdd97a2ced1f341fd8a24d1497529a25b, '2025-04-20 15:03:36.000000', 44, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-24 23:59:59.000000'),
(0xe07d4e5bfbf5453b9a2ceeab471e72e8, '2025-04-17 23:09:12.000000', 19, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x24baed2d054e449cb999aaf9a294bf80, NULL),
(0xe1dee520fe2240ce9930e90309975076, '2025-05-05 21:05:56.000000', 75, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-07 23:59:59.000000'),
(0xe38652d1f3da4629b854a80d352a13ca, '2025-05-03 22:24:42.000000', 68, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, '2025-05-05 23:59:59.000000'),
(0xe5382057ca284eb186bddd5291a5699f, '2025-04-18 16:49:12.000000', 27, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xa0b56a801b0d400eae58b148e3029b59, NULL),
(0xe908f84bbb434564a62d5506f59fe257, '2025-05-27 22:23:37.000000', 105, 0xa180561dbf4349e080760c5c466136c0, 0xf1d838356643402c801c4a757502e799, '2025-05-29 23:59:59.000000'),
(0xea9d7181b13c446098af371fb24f17b3, '2025-04-21 21:33:06.000000', 63, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x7936adf839714b7eb53840c8fceca645, '2025-04-23 23:59:59.000000'),
(0xeb0dc3b0a931432089edac13835bfbb1, '2025-05-05 22:12:26.000000', 82, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-07 23:59:59.000000'),
(0xeb1e5e514be5436c9a2c0f6ec9431c0c, '2025-05-06 08:39:40.000000', 85, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-07 23:59:59.000000'),
(0xec4cbc62bc484b22b353d8924af4f6d7, '2025-05-06 18:11:17.000000', 96, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-08 23:59:59.000000'),
(0xec98168db59a4fdc9223244e68223a52, '2025-05-05 22:18:12.000000', 83, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xe30bcc340d064478b6bf31a2475e1fbd, '2025-05-07 23:59:59.000000'),
(0xed68f8f8364a4536aadb6f6a84cd7f83, '2025-04-18 15:51:07.000000', 25, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xaaf0d39fd48544a5848ed739c59efcf8, NULL),
(0xf25fe0193f5e42b19b14836a20e93f3d, '2025-04-17 22:56:41.000000', 16, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0xf312ddc8cf2643488fa6b2b31d76c9a8, '2025-04-18 23:41:24.000000', 29, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xaa1302104d09489ea2c6b26cb13269c9, NULL),
(0xf64d27e9fa9b44a49c263b3574b60397, '2025-05-28 18:04:05.000000', 107, 0xa180561dbf4349e080760c5c466136c0, 0xf1d838356643402c801c4a757502e799, '2025-05-30 23:59:59.000000'),
(0xf7dc5ebb3e8045669ea4d89884dc7a34, '2025-04-20 15:07:03.000000', 45, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-24 23:59:59.000000'),
(0xfa20cff81da944648105e3e429843bad, '2025-05-30 10:24:01.000000', 115, 0xb653efc8f9fe4ca880bcbc63ae82418b, 0x19f7bb51280f440b91e8e451679f07f4, '2025-05-31 23:59:59.000000'),
(0xfca48a071ae84e209493ea674c06c7f4, '2025-04-20 14:32:21.000000', 37, 0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x064338789eae4291ad71af4af57a3934, '2025-04-24 23:59:59.000000'),
(0xfdb813363e184f5d8370c4abdeffce33, '2025-04-18 16:50:33.000000', 28, 0x7aea20848d0841ddb3575d9f242c3b9f, 0xaa1302104d09489ea2c6b26cb13269c9, NULL),
(0xfdfb9ba45c5a4a68866152a0262aa582, '2025-04-18 13:40:07.000000', 24, 0x7aea20848d0841ddb3575d9f242c3b9f, 0x4655c566184811f09d2700155d851915, NULL),
(0xfe0987a2f270444fbad1ecdf6f3546cd, '2025-05-06 08:28:03.000000', 84, 0x4e916c2f24624a0eb1322ad0699a26c3, 0x40a1a6c5dca94b7e9066229c59d6453c, '2025-05-07 23:59:59.000000');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `id` binary(16) NOT NULL,
  `product_price` bigint(20) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `book_id` binary(16) DEFAULT NULL,
  `order_id` binary(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `product_price`, `quantity`, `book_id`, `order_id`) VALUES
(0x0008abf7bb6c417dad233e537e6eb73f, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0x15bd4e7760d648eb99eb007d5b8d1568),
(0x008c5aca3d8649af9bebe979fe49dd9a, 70000, 1, 0x1f79f6b0184811f09d2700155d851915, 0x514f1d08e04f4faea4468d515fb387ad),
(0x00d5f1925c51445c91e4d91c35d2c4a2, 70000, 1, 0x1f79f6b0184811f09d2700155d851915, 0xf25fe0193f5e42b19b14836a20e93f3d),
(0x02d6912b771349e387b014e88615be40, 150000, 1, 0x1f79fc81184811f09d2700155d851915, 0xae668ecf9b86471e97f2aa240c265fad),
(0x0377f34e11f44cd5be1cf52a6510ca73, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x0953e559b596443b9a7f312787659ce9),
(0x0436a94873014beb8eb4598e60668b21, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0x7d76b25984d24fac97ee65af9d9252d0),
(0x059bf3e37ef14d1792d627f0dd44cbbf, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0xfdb813363e184f5d8370c4abdeffce33),
(0x0729266215ea4e22973e2fc716a58748, 75000, 1, 0x1f79f7a5184811f09d2700155d851915, 0x7082e0dbc81d4d6db4c7f8ba15487c3d),
(0x07eda47b06074be2b9cf03423d6571d8, 60000, 1, 0x1f79f9b2184811f09d2700155d851915, 0x5fe2969c8a804aa1b942cee59d9cac2f),
(0x08c6552052e64dff95effd591084c5ba, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0x87be39a1d3d54416ac54dd57c97180b0),
(0x08d7f07e06dd4d38beee9c4f3e030420, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x301446dfc1c6493b84157a0f6745d42a),
(0x090d9a26dbcb4444a49724db984f928c, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0xe38652d1f3da4629b854a80d352a13ca),
(0x0912d3bd05dc47a7ad4c3faa98f5c7f2, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0x13a88ab280184fd784383d973df33bf6),
(0x0a6391ef40564ab68b05d7afc5470cbc, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0x0736065944bd492a847d408417fb4b58),
(0x0a70b6a6a0024745a022a1f3af3f45c6, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x486707ccb6dd489ba2a761ae9a5cbda0),
(0x0f01342b695e42ad9151d3f3cec8c7e8, 65000, 1, 0x1f7a084c184811f09d2700155d851915, 0x7bd3f5eb03d747ef9fb71a6b2d252626),
(0x0fa69af73056420da4ff3d841f48245b, 75000, 1, 0x1f7a1369184811f09d2700155d851915, 0x48f79248d52f4f9c9a50baa29baa373d),
(0x10c4e47c95854e8f8c2a05e493a8b33a, 65000, 1, 0x1f7a084c184811f09d2700155d851915, 0xf64d27e9fa9b44a49c263b3574b60397),
(0x10d1ec46c4994b37a151d08d7c95e859, 85000, 1, 0x1f79faa3184811f09d2700155d851915, 0x17604455fa434f8dbcac2ccdaaff9783),
(0x1297488aef734ef4978c0a5854a3852e, 105000, 1, 0x1f7a0060184811f09d2700155d851915, 0x8182a042e16349a4ab68db0120b6373f),
(0x1685c3f12a7c4965a0d263e535e3ed2c, 105000, 2, 0x1f79f5af184811f09d2700155d851915, 0xb29e02ace4c74a72b985b6b39a3cd8de),
(0x17cb906d65974463aaf7db088782442a, 95000, 1, 0x1f7a0c87184811f09d2700155d851915, 0x1376c16a547647be94895b5127fc2d8e),
(0x1968b611127341debfd8d67c76b74c75, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0x8498e8c529474e72b4426b4a04935af6),
(0x1a120fdcbd904ebb96279229c9394ca9, 92000, 1, 0x1f7a106b184811f09d2700155d851915, 0xec4cbc62bc484b22b353d8924af4f6d7),
(0x1a5a35e456e04b06909558aee892bdab, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0x813830368c784a7ba6689c3d845d0610),
(0x1a65e96cf1d445e4b92a48cf1f0c5403, 75000, 1, 0x1f7a1369184811f09d2700155d851915, 0xe07d4e5bfbf5453b9a2ceeab471e72e8),
(0x1aa57d269178435dae8b22de2deaa97f, 110000, 1, 0x2aeda0b31a6b4b19b100aa45b92a21c4, 0xbb537a27b54a4c51bfbd756884365a93),
(0x1ebc66d3f0144efb81be51e8479f3ba3, 70000, 1, 0x1f7a0968184811f09d2700155d851915, 0x7bd3f5eb03d747ef9fb71a6b2d252626),
(0x1fbac213d9c049858e4a4c3e3190ed00, 120000, 2, 0x1f79b19b184811f09d2700155d851915, 0x5fd5be861cda4ceb88997e4e57df133c),
(0x2073128e763f42f79a9bfa3bf464d4ae, 89000, 1, 0x1f7a0e9f184811f09d2700155d851915, 0x72b113c31abb4dfeb6d5b814a32867e5),
(0x211f86c17dc342a2a46cd3f06c9bef4f, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0xbe5ba05891b44799b4fcf945306f47c1),
(0x237d4a1eb07f4ee4bc743e3e99a5a466, 70000, 6, 0x1f79f6b0184811f09d2700155d851915, 0x9b4cd1d45458460e8d9f9468ac427917),
(0x247cd02a3a274ab79b9dd9799fe739d1, 75000, 1, 0x1f7a0f89184811f09d2700155d851915, 0x6a8666a4f1494a56ae5108b8fbf1e9d9),
(0x2743aee115d845b4bf95d741ded1c099, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0x929fb3cb7f8a4ad98aa1552bb036814d),
(0x27b831c80cec4f0fbea333b74b595166, 189000, 1, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0xfa20cff81da944648105e3e429843bad),
(0x286c0fa94e5b48ddb4acb8503abe0b8a, 135000, 1, 0x1f79fe76184811f09d2700155d851915, 0xac17c3fd36a2484ea78016c7b2af0ae0),
(0x28d041769c274480a9555c47902f5205, 92000, 1, 0x1f7a106b184811f09d2700155d851915, 0x11bbf51f9d294706afdee5816851ae4b),
(0x2a6a058eb84b4dda92d097bc50a8e2aa, 189000, 1, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0xd569c5cd5d8d4f45a2fb6bf05ff1ff72),
(0x2b7af0e815ae4e76a3f3d90091dca766, 130000, 2, 0x1f79e2f9184811f09d2700155d851915, 0x0cf244cb606c4e3e9c6137bdff1f4fe1),
(0x2ee1c932802b4dcba4698ca1b212aeca, 210000, 1, 0xd10ca66dd1f648899738346b2cb1a653, 0xfa20cff81da944648105e3e429843bad),
(0x2fd8951341a24591bc4e0822acceb518, 99000, 1, 0x1f79ff75184811f09d2700155d851915, 0x08821028ab704661a750a4f844842c43),
(0x307e7c3725a8466888ba4bbf4a5b3ff2, 189000, 1, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0x466fd5c1e75d4184b5242a53775bff2d),
(0x30a82d48ebe84042a63610984789f9aa, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0x97487f90ccba40ac8262879c15c49a34),
(0x319969fa14c8499bb14e757e3acefdb1, 210000, 1, 0xd10ca66dd1f648899738346b2cb1a653, 0x0953e559b596443b9a7f312787659ce9),
(0x3243f303e41b4f6ead2b88154eb68b0b, 70000, 1, 0x1f79f6b0184811f09d2700155d851915, 0x2e534b23c9c04a7b964a8ef1f7f18e65),
(0x32714b6ccf5348d790bbfe72705b246e, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0x8498e8c529474e72b4426b4a04935af6),
(0x34d8a79b4bd84c259039319493552113, 120000, 1, 0x1f79b19b184811f09d2700155d851915, 0xe908f84bbb434564a62d5506f59fe257),
(0x3662f103cc4347a383fcaf6a7f30f584, 120000, 1, 0x1f79b19b184811f09d2700155d851915, 0x56da5a9493f14237a9ff57e7e3ed901d),
(0x3692720773884871bb24e4a95768367f, 115000, 1, 0x1f79e4d6184811f09d2700155d851915, 0xe908f84bbb434564a62d5506f59fe257),
(0x371b3da586b04fb6ba06d8fa525d3883, 80000, 1, 0x1f79f89b184811f09d2700155d851915, 0xf25fe0193f5e42b19b14836a20e93f3d),
(0x3819fdc85f1f463f81c23ce4babd4412, 70000, 2, 0x1f79f6b0184811f09d2700155d851915, 0xfca48a071ae84e209493ea674c06c7f4),
(0x384a104670fa4b4395b4590d33093192, 75000, 1, 0x1f79f7a5184811f09d2700155d851915, 0x57352187b2a8467c889bdd342aa11d6a),
(0x3a8a6ee733644c019de60ba7be57582a, 75000, 1, 0x1f7a1369184811f09d2700155d851915, 0x8ffcc55747cb4c7bb045940495418567),
(0x3c8e80eebb4848fd9dacf2ec73663e67, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0x4015365d9c72473e9d18557c4b187050),
(0x3cba993d64424ea9be2b158a89714255, 115000, 1, 0x1f79e4d6184811f09d2700155d851915, 0x4b11f1dd57634aa9863e9653aeccd35f),
(0x411c1a9a455f411e96602a56f8884b15, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0x4015365d9c72473e9d18557c4b187050),
(0x414094f157a44ecda99defd1b72ee92c, 135000, 1, 0x1f79fe76184811f09d2700155d851915, 0x94b18192e0fd4e0fa0db560c96184219),
(0x42f90e69fdfc4a71b43883e885573d8f, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0xb8c3cbdcdc4341839c99e154951dd997),
(0x433d7258d1fd402cabb1c3bcc39518e3, 70000, 1, 0x1f79f6b0184811f09d2700155d851915, 0xed68f8f8364a4536aadb6f6a84cd7f83),
(0x43dc065c41574e95beecc375db33efe2, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0xd8948e00f3924033abad11876edfa269),
(0x4455b9c225704695959ea560b05d77aa, 80000, 1, 0x1f79f89b184811f09d2700155d851915, 0x2e534b23c9c04a7b964a8ef1f7f18e65),
(0x44ee61b321e54ac6b4207e99adf48d25, 68000, 1, 0x1f7a118f184811f09d2700155d851915, 0x8ffcc55747cb4c7bb045940495418567),
(0x452247ad178a4a7d86fadfab24e62040, 120000, 1, 0x1f79b19b184811f09d2700155d851915, 0xd7e6925eb1a34b1581f1bfcc3a39c4e0),
(0x453824b220b34a05a038cdc497fcc0e2, 85000, 1, 0x1f79faa3184811f09d2700155d851915, 0x593f7d57bde94ebd9a4a0353a87a175b),
(0x46cd093b66954e52800d56349c95c167, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0xcbfdf99d47a744a095cb00b8a845c340),
(0x47213a40eef74091a2caa91e12a3c97e, 95000, 2, 0x1f79e5e3184811f09d2700155d851915, 0x514f1d08e04f4faea4468d515fb387ad),
(0x478a12fabdba4f0da14bab92dd8cbe82, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0xab6a6472bcea4810961bb090cc570def),
(0x4955d8238b2d4a9a84057cc978f2b39e, 120000, 1, 0x1f79b19b184811f09d2700155d851915, 0x12aaedb9a7484b48997eb6bfc259bbef),
(0x4a1b5de1590741d3a3cba70faec98cb0, 150000, 1, 0x1f79fc81184811f09d2700155d851915, 0xfe0987a2f270444fbad1ecdf6f3546cd),
(0x4ad236b695fa41b99ac7553724e7010a, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0xd8edfcbfdc3a42b4a616b0e5ede1376a),
(0x4b640a69512e4c8b80d67da2a94b1d8f, 189000, 1, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0x1d88f716b61240969491b62287900404),
(0x4cc592d6942147fd93eb489c422eb92c, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0x9ee5f30cd00d40aeb6e71bd251c6b758),
(0x4e9f8b2972fc48aca93d8e5230b8d505, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0xc86db18a4897467babcc2a2681ab0888),
(0x4ec04857a4c24ef5ab77bb3e4cd735b2, 75000, 1, 0x1f79f7a5184811f09d2700155d851915, 0xd8edfcbfdc3a42b4a616b0e5ede1376a),
(0x4fe51c99512c4ea7ae478e891c5aa725, 80000, 1, 0x1f79f89b184811f09d2700155d851915, 0x0cf244cb606c4e3e9c6137bdff1f4fe1),
(0x5051d3582987484ab8c7a2dc6d5d2abd, 75000, 1, 0x1f79f7a5184811f09d2700155d851915, 0xbcc07161b9664b11b96e05c76672513b),
(0x508cad7b48f24c8594b770baa68e31f8, 200000, 1, 0x1f79fd7e184811f09d2700155d851915, 0x94b18192e0fd4e0fa0db560c96184219),
(0x53d4a8eaaba64a988f64a86d8836b1cf, 85000, 1, 0x1f79faa3184811f09d2700155d851915, 0x9b4cd1d45458460e8d9f9468ac427917),
(0x54c6bf4231f94cfda69f374d6b2581e7, 95000, 1, 0x1f7a06f0184811f09d2700155d851915, 0xfdb813363e184f5d8370c4abdeffce33),
(0x55bfe17228ad4800bcdafead4749729f, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0xd7661704f0ed4f26862a70f455065a3f),
(0x5744e298c78e4d43a14a5bab1bbb8558, 60000, 1, 0x1f79f9b2184811f09d2700155d851915, 0x57352187b2a8467c889bdd342aa11d6a),
(0x59b9e2388efe42f8b83ba46296f58342, 70000, 1, 0x1f7a0968184811f09d2700155d851915, 0xf64d27e9fa9b44a49c263b3574b60397),
(0x5b3b97ca9b074d0299e39d26dfc2c9bb, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0x929fb3cb7f8a4ad98aa1552bb036814d),
(0x5b6e71a025154b948da6132fceca74ef, 98000, 1, 0x1f7a041c184811f09d2700155d851915, 0x6633d15a94ed4edba6f2df2a80d1691c),
(0x5c89dc61b6394db0ad805c804d106faa, 70000, 2, 0x1f79f6b0184811f09d2700155d851915, 0x621ccd4d8c6f4d3dbdec3fe245fe49be),
(0x5d6df3ddeac54f49be9d1e34ff76d4ec, 70000, 1, 0x1f79f6b0184811f09d2700155d851915, 0xfdfb9ba45c5a4a68866152a0262aa582),
(0x5e59daeee5d646c6bb00a2603ce59b31, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x56da5a9493f14237a9ff57e7e3ed901d),
(0x5e78838c672c45f69a0677a9909371bb, 189000, 1, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0x12aaedb9a7484b48997eb6bfc259bbef),
(0x600581774e7743c9bbfbdf43ab4cd875, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0x301446dfc1c6493b84157a0f6745d42a),
(0x62dbddd90716428997b35fc27290c0ca, 60000, 1, 0x1f79f9b2184811f09d2700155d851915, 0x9b4cd1d45458460e8d9f9468ac427917),
(0x65db152e48ac47a793d197f0d50ca7d9, 95000, 1, 0x1f7a06f0184811f09d2700155d851915, 0x7d76b25984d24fac97ee65af9d9252d0),
(0x687bf80971b8466db8160aa3bfc1b8b6, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0xd87103bb59fb437cbbe1c33e43fdb2b3),
(0x688c1b4c1f354288b4645fc57fb80f00, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0xe5382057ca284eb186bddd5291a5699f),
(0x6904b5fcf60f416da13c8ef91b425947, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0xa841f0a46c1d449d8bbec98282f2ba7c),
(0x69a6e28a322d4102bbfbb5a8c0229988, 72000, 1, 0x1f7a04ff184811f09d2700155d851915, 0xfdb813363e184f5d8370c4abdeffce33),
(0x6a3d557ead39428998e31816aa2becc2, 75000, 1, 0x1f79f7a5184811f09d2700155d851915, 0x514f1d08e04f4faea4468d515fb387ad),
(0x6bba4a2b35b44b878b6bd231232cbd54, 115000, 1, 0x1f79e4d6184811f09d2700155d851915, 0x56da5a9493f14237a9ff57e7e3ed901d),
(0x6db18248f6564463930fe2c870b8aaeb, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0xea9d7181b13c446098af371fb24f17b3),
(0x6dd20ac867384736866a31010ea9eba5, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x4b11f1dd57634aa9863e9653aeccd35f),
(0x6df5b10791e14303bfe9bded11665b79, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0xb3ef5caa24c24eb88f47501ad06d3cff),
(0x6e2ed297cbf041d5bab97d499b4a2b23, 65000, 1, 0x1f7a084c184811f09d2700155d851915, 0xb0bc7465a0fc44c1b66d4dd6810d03ba),
(0x6e544f27ea9b4d27a5900f1140c41194, 65000, 1, 0x1f7a084c184811f09d2700155d851915, 0xb0102746929b4c638ff39a7efdd9f77d),
(0x6e6b5d958f554bd9952266e0674c3bfc, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0xb29e02ace4c74a72b985b6b39a3cd8de),
(0x6ef59e9cad764fc2a4920ac45c1e7bbc, 105000, 1, 0x1f7a0060184811f09d2700155d851915, 0x08821028ab704661a750a4f844842c43),
(0x6f376222fd9d422faabb97a4f827c2c0, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0xb8cbf2fdf8c64cecb7f4f85a948a5b4c),
(0x71e0f0dbde054ba3959d2e6e9525a19a, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x8d346152658549c2b8af4796f1de696c),
(0x76d669a1eb29405b9cecec367f7a5db7, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0xd7661704f0ed4f26862a70f455065a3f),
(0x77d74a0fddb8491eb6aed0eaea996db5, 68000, 1, 0x1f7a118f184811f09d2700155d851915, 0xe07d4e5bfbf5453b9a2ceeab471e72e8),
(0x78edbda8a86d453abf460e76d14e004c, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0xba238f8fd6fa41bb9c40be34af7a54cf),
(0x7971c976a6654186b19925920c16ecb8, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0xb391fe307a534dedb1159a0d5b68e0a2),
(0x7b8b9ba5c7194962a1c63f664735181b, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0xea9d7181b13c446098af371fb24f17b3),
(0x7e5b73e2725c475aadabaafc90deb4b6, 120000, 1, 0x1f79b19b184811f09d2700155d851915, 0xbb537a27b54a4c51bfbd756884365a93),
(0x802c9d3011134f5887bb2c09d82c8abf, 75000, 1, 0x1f79f7a5184811f09d2700155d851915, 0xed68f8f8364a4536aadb6f6a84cd7f83),
(0x820f90dbdae74af182ceafda20eaa343, 75000, 1, 0x1f79f7a5184811f09d2700155d851915, 0xf312ddc8cf2643488fa6b2b31d76c9a8),
(0x8384df7380aa44839aed3e2bf57bdc40, 115000, 1, 0x1f79e4d6184811f09d2700155d851915, 0x2710e7ab24ae4d8aa144a41cea0eb487),
(0x83941f1d987c4b7eacd36d7f053d2a61, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0xb8cbf2fdf8c64cecb7f4f85a948a5b4c),
(0x8483f798b5054385a8f036bbe762e44e, 50000, 1, 0x1f79fb8d184811f09d2700155d851915, 0xa53c3618eb354860a4190f2b7c1b0981),
(0x851a18e471b14faebb0b68da5613db50, 115000, 1, 0x1f79e4d6184811f09d2700155d851915, 0xfa20cff81da944648105e3e429843bad),
(0x86d5fc642c1a4ce8ba3282f10a472394, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x87be39a1d3d54416ac54dd57c97180b0),
(0x8ac774d494bb4e42b7c4aa29290d370b, 98000, 1, 0x1f7a041c184811f09d2700155d851915, 0x942c2199754f4322a568848dfe53355e),
(0x8aeb101a373d43bc9f49d91851831e83, 135000, 1, 0x1f79fe76184811f09d2700155d851915, 0xbb537a27b54a4c51bfbd756884365a93),
(0x8c69517162dd4139a514d682e40c31b4, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0xb8c3cbdcdc4341839c99e154951dd997),
(0x8ecc462aa1ae43f493633c8af6785bf4, 110000, 1, 0x1f7a032c184811f09d2700155d851915, 0x0d0443a9d31848a3bb31cc7d1f82bfb4),
(0x8fccab49ec214869923a434ca102c906, 115000, 1, 0x1f79e4d6184811f09d2700155d851915, 0xd7e6925eb1a34b1581f1bfcc3a39c4e0),
(0x90055c3e6ff74387b4ae6356303e66ad, 80000, 1, 0x1f79f89b184811f09d2700155d851915, 0xbcc07161b9664b11b96e05c76672513b),
(0x90ce72c20c634cef9c17aeb5621a7290, 150000, 1, 0x1f79fc81184811f09d2700155d851915, 0x6ea2420419034d85853963cf3a35ff36),
(0x91a74bb3dd234b1fb17062c36eb55173, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0xc8a81498469f42fc8b6d4311bac2216c),
(0x921fa694848b4e42b34b1d18db5b166a, 70000, 2, 0x1f79f6b0184811f09d2700155d851915, 0x6b8e078641d34c21b708b996d5ce5cb6),
(0x93eabd6666514100a1f6b634228cc817, 120000, 1, 0x1f79b19b184811f09d2700155d851915, 0xab6a6472bcea4810961bb090cc570def),
(0x95d43e94984e469f9b9380892c261bac, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0x433bb097046d4856aef8f0cf85be84ea),
(0x9740b8c795f4430ea98cd89f14f2a8f5, 75000, 1, 0x1f7a0f89184811f09d2700155d851915, 0x2b5c50a7c15649ce8f7189c8160cae60),
(0x986b975d8f254b5e8bdf60e24748ea61, 105000, 1, 0x1f79f5af184811f09d2700155d851915, 0xc8a81498469f42fc8b6d4311bac2216c),
(0x98b7804cf2a048b58529bbf99938ad50, 105000, 1, 0x1f7a0060184811f09d2700155d851915, 0x2a92c7abc59b47e98311c71cd171196e),
(0x9b8f1ee41f154648b797768297317dcc, 70000, 1, 0x1f79f6b0184811f09d2700155d851915, 0x78e39daead3f43f5be744e9353e55b59),
(0x9cf95b9918ab43bdbccd710e6bf498e3, 75000, 1, 0x1f79f7a5184811f09d2700155d851915, 0xb53566ed4d364201b1686bb9b1d25fc4),
(0x9d9e750013ee47d4b3cbd95917667f90, 189000, 1, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0xba238f8fd6fa41bb9c40be34af7a54cf),
(0x9f1a12a63f974db4bc186a653f180043, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0xdd97a2ced1f341fd8a24d1497529a25b),
(0xa03b582dd4dc477cb030991b2bf18ca5, 92000, 1, 0x1f7a106b184811f09d2700155d851915, 0xeb1e5e514be5436c9a2c0f6ec9431c0c),
(0xa0d224b597ad4abba1b3a028f7788d91, 75000, 1, 0x1f79f7a5184811f09d2700155d851915, 0x9b4cd1d45458460e8d9f9468ac427917),
(0xa0f8386a43714a509ae168138525c11f, 70000, 1, 0x1f79f6b0184811f09d2700155d851915, 0x79ee03118dd941e1b3675cf12268a7f8),
(0xa44df5b2887e42e19be17ea5c705ba00, 150000, 1, 0x1f79fc81184811f09d2700155d851915, 0x94b18192e0fd4e0fa0db560c96184219),
(0xa6c123242c654fb8b9e14d6552507132, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0x37aaa826cb2b4d279eb3c7e535727ae0),
(0xa73bfbc240ff4e5d84fad1f9fb52341f, 88000, 2, 0x1f7a05fe184811f09d2700155d851915, 0x8498e8c529474e72b4426b4a04935af6),
(0xa9be1bed20e347bea317caea05608fee, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0xd97d27cfbf5e4397be2fd9869b2ea5ba),
(0xabe5952436024364863b5f21236d59ef, 120000, 1, 0x1f79b19b184811f09d2700155d851915, 0x1b9f65865f5e42a6b9905b526f2879c6),
(0xad9fe10c5d4b425ba346128e95091261, 75000, 2, 0x1f7a1369184811f09d2700155d851915, 0x8f83604090ee45b492e52ba8fb02e41a),
(0xae19e7f963d5459e842920e7294cfd72, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0x13a88ab280184fd784383d973df33bf6),
(0xaf998a1e11144c8a905786aaa666e4fe, 80000, 1, 0x1f79f89b184811f09d2700155d851915, 0xdcbe2ff57a2e4ae7bea0d1e5102345ff),
(0xb066f84deba54384abe52baa3dabf7b4, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0x21fe26ece06a4747b5c645b2d636d1ff),
(0xb0b855b6152545339315c861d289fc87, 70000, 2, 0x1f79f6b0184811f09d2700155d851915, 0x62ea0eebd8f845ca9635d0b08a0ed00e),
(0xb3e536254fdf45c9ba81bdce448b2b86, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x79ee03118dd941e1b3675cf12268a7f8),
(0xb60a715c0b6c4fcd998d29bbab7118f0, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0xc8a81498469f42fc8b6d4311bac2216c),
(0xb66aae5c54e243acabb4951dc6099e49, 80000, 1, 0x1f79f89b184811f09d2700155d851915, 0x57352187b2a8467c889bdd342aa11d6a),
(0xb6fdb63aa3af4b0fb10e7a8e0288bc0f, 189000, 1, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0x8d346152658549c2b8af4796f1de696c),
(0xb7bbcbd89c434e61b1c203116588aa2c, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x9b4cd1d45458460e8d9f9468ac427917),
(0xb886c151965147bdb48ba092e3e17af3, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0xbaa0c3d84ba747b29b6a49a5c7863ca9),
(0xb8cb238483fe4d46b9b4b20650d613fc, 99000, 1, 0x1f79ff75184811f09d2700155d851915, 0x17604455fa434f8dbcac2ccdaaff9783),
(0xbadfbc121670490096286a87f9c6f639, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0x78e39daead3f43f5be744e9353e55b59),
(0xbf9d0da3fdc144adb97d231e8b093597, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0xcbfdf99d47a744a095cb00b8a845c340),
(0xc082c8c974884921978fcec75cd647b4, 60000, 1, 0x1f79f9b2184811f09d2700155d851915, 0x8d346152658549c2b8af4796f1de696c),
(0xc15382c4f79e4b52b0b7fc80a1c43108, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x3dfd93346ad24548819df4a414c24799),
(0xc5604da3b448456bb55897d57a821f41, 80000, 1, 0x1f79f89b184811f09d2700155d851915, 0xf312ddc8cf2643488fa6b2b31d76c9a8),
(0xc57715e2e5234d5abe7231da37bc277e, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0xa9c5f3ddc39745f28e87984cfb8bcd47),
(0xc6662b4459ca476293051e13d6e2fe8c, 120000, 1, 0x1f79b19b184811f09d2700155d851915, 0x1d88f716b61240969491b62287900404),
(0xc6bcc50e133b489b8bcbf4bb6f473099, 135000, 1, 0x1f79fe76184811f09d2700155d851915, 0x17604455fa434f8dbcac2ccdaaff9783),
(0xc74374b249604a64aa367e78aa5db398, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0x39a6b981f2a846479901c850a90c531e),
(0xc8ea56800c7a4b1fb219c3128bfc2f10, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0xb391fe307a534dedb1159a0d5b68e0a2),
(0xc9d6fac8ab4b47e4a99ab9bffa039533, 70000, 1, 0x1f79f6b0184811f09d2700155d851915, 0x7082e0dbc81d4d6db4c7f8ba15487c3d),
(0xcb7ebb1751e94799a99f15c69263d9d2, 70000, 1, 0x1f79f6b0184811f09d2700155d851915, 0x89542291460f442ba58ef36565eca891),
(0xce0de3ff7202413fa3d1f3c1bd4778c8, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0xcf0946d2469e4a298393798c61c0979d),
(0xcebbcb76534a4cfc936591634671e7d1, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0x15bd4e7760d648eb99eb007d5b8d1568),
(0xd19fd81a302040d4a66af0953a9efff7, 72000, 1, 0x1f7a0dc3184811f09d2700155d851915, 0x1376c16a547647be94895b5127fc2d8e),
(0xd23c054b23e44138a053af5856f1fce8, 92000, 1, 0x1f7a106b184811f09d2700155d851915, 0x6a8666a4f1494a56ae5108b8fbf1e9d9),
(0xd3ab30d0258c4c43b3e181371a32dd68, 85000, 1, 0x1f79faa3184811f09d2700155d851915, 0xa53c3618eb354860a4190f2b7c1b0981),
(0xd43dcfb8b7e542b9a8432cf6cd37e21b, 70000, 2, 0x1f79f6b0184811f09d2700155d851915, 0x21f6b5f93bbd41e4ae4472b8c4a480ed),
(0xd65b6a70e998418fb1a3936704085ec0, 60000, 1, 0x1f79f9b2184811f09d2700155d851915, 0xb22df640ca634ae984168faa40011829),
(0xd93dd89c50b243ea9aa879a95aa1d7b0, 65000, 1, 0x1f7a084c184811f09d2700155d851915, 0xeb0dc3b0a931432089edac13835bfbb1),
(0xd9db55f09c914b0298aa714cfacd8fd0, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0xe5382057ca284eb186bddd5291a5699f),
(0xdc791bc6579b4aa786cd8e9949cec8a0, 189000, 1, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0xbb537a27b54a4c51bfbd756884365a93),
(0xdd2c457c531b413faa6ec72a1f717ecd, 70000, 1, 0x1f79f6b0184811f09d2700155d851915, 0xdcbe2ff57a2e4ae7bea0d1e5102345ff),
(0xde146c95557a481b9ebb41780d0d088e, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0xe1dee520fe2240ce9930e90309975076),
(0xdfceead1e3c4496bb90876187927dda3, 95000, 1, 0x1f7a0146184811f09d2700155d851915, 0x8182a042e16349a4ab68db0120b6373f),
(0xe22a73d8c7664a49b9fa0f43a3f8b882, 120000, 1, 0x1f79b19b184811f09d2700155d851915, 0x4b11f1dd57634aa9863e9653aeccd35f),
(0xe26209a95cb9417ab99f5621b9b63f04, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0x7082e0dbc81d4d6db4c7f8ba15487c3d),
(0xe461dd13710840a196e600b7bab1f96c, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0x9b4cd1d45458460e8d9f9468ac427917),
(0xe476af8d7aaf4108a7ed6eec8c2af03c, 92000, 1, 0x1f7a106b184811f09d2700155d851915, 0x72b113c31abb4dfeb6d5b814a32867e5),
(0xe613a8313b8d4dfea77d4e31fb88c739, 88000, 1, 0x1f7a05fe184811f09d2700155d851915, 0xf7dc5ebb3e8045669ea4d89884dc7a34),
(0xe6a8c6402793401ba160af6fca12fc77, 85000, 1, 0x1f7a0a70184811f09d2700155d851915, 0x7bd3f5eb03d747ef9fb71a6b2d252626),
(0xe83890574aa94c6aac47dae2ec7f14fa, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x813830368c784a7ba6689c3d845d0610),
(0xe926a44b9d4848fe837f55325744a2bd, 130000, 1, 0x1f79e2f9184811f09d2700155d851915, 0x6038c459cb274a3aadcc1fed6ae32f98),
(0xe95677ccee644b4a807346a127ea1b60, 115000, 1, 0x1f79e4d6184811f09d2700155d851915, 0xab6a6472bcea4810961bb090cc570def),
(0xe9bd09377f6d4e03b279c2c78381c659, 150000, 1, 0x1f79fc81184811f09d2700155d851915, 0xac17c3fd36a2484ea78016c7b2af0ae0),
(0xec933ec9bc0d42cfba044db35f2c1649, 120000, 1, 0x1f79b19b184811f09d2700155d851915, 0x59516b295c1643319743b8a3813062e2),
(0xed48e8e5809f4d6d8f532f908ed076c5, 95000, 1, 0x1f7a0c87184811f09d2700155d851915, 0x633fcdd799214dd7932a263ea4906b80),
(0xefd9d267b56946b8bd0dd744de0cead8, 75000, 1, 0x1f7a0f89184811f09d2700155d851915, 0x72b113c31abb4dfeb6d5b814a32867e5),
(0xefed30e4ccb544a392d5bf46c2121d0b, 50000, 2, 0x1f79fb8d184811f09d2700155d851915, 0x991c622a50e74bcc9a5e149ae74ac370),
(0xf082a9efffef4ba4a6fad322cd746b71, 75000, 1, 0x1f7a0f89184811f09d2700155d851915, 0xeb1e5e514be5436c9a2c0f6ec9431c0c),
(0xf1498229fdfe44709150a8b0db6fd839, 60000, 1, 0x1f79f9b2184811f09d2700155d851915, 0xdcbe2ff57a2e4ae7bea0d1e5102345ff),
(0xf1c4b43c17ec46e596166c968a8dd388, 189000, 1, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0x4ce1b25901614820997cff3b1c5e65eb),
(0xf23ecb231047470485e387e3589c53f6, 70000, 2, 0x1f79f6b0184811f09d2700155d851915, 0xaaf09ef061ef40deb7b5ec2c63ee0eab),
(0xf2b9cd6f86824b18ad3793b815212c38, 99000, 1, 0x1f79f426184811f09d2700155d851915, 0x8498e8c529474e72b4426b4a04935af6),
(0xf2d5a42c7dd84a31951aa644ef34d71a, 60000, 1, 0x1f79f9b2184811f09d2700155d851915, 0x593f7d57bde94ebd9a4a0353a87a175b),
(0xf2f0b62a125e41a5bb63a925b0a472ea, 98000, 1, 0x1f7a041c184811f09d2700155d851915, 0x0d0443a9d31848a3bb31cc7d1f82bfb4),
(0xf44deb269e9e487baa137c47e0fb3ffe, 135000, 1, 0x1f79fe76184811f09d2700155d851915, 0xfe0987a2f270444fbad1ecdf6f3546cd),
(0xf6299d123d1b429ea91857b4ba1d9585, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0xe38652d1f3da4629b854a80d352a13ca),
(0xf89b5a83ece042afad3b0644818a8873, 200000, 1, 0x1f79fd7e184811f09d2700155d851915, 0xae668ecf9b86471e97f2aa240c265fad),
(0xf9d15b4310c147c895d2d949418cc491, 135000, 1, 0x1f79fe76184811f09d2700155d851915, 0x6ea2420419034d85853963cf3a35ff36),
(0xf9ed75b3f9a34f53b8181f1efd0e1e47, 80000, 1, 0x1f79f89b184811f09d2700155d851915, 0xb22df640ca634ae984168faa40011829),
(0xfbb54224f9814f1d9635b4c2b40990c3, 68000, 1, 0x1f7a118f184811f09d2700155d851915, 0x48f79248d52f4f9c9a50baa29baa373d),
(0xfc65d566274c44df926e444390bd4cc5, 72000, 1, 0x1f7a1281184811f09d2700155d851915, 0xe07d4e5bfbf5453b9a2ceeab471e72e8),
(0xfd0265f6971640cb9d311c6fb6e9d50f, 95000, 1, 0x1f79e5e3184811f09d2700155d851915, 0x59516b295c1643319743b8a3813062e2),
(0xfe3a9589c8924172bb4512f65b0f40f4, 75000, 1, 0x1f7a0f89184811f09d2700155d851915, 0xec98168db59a4fdc9223244e68223a52),
(0xfe593291df65461388ba373d764e2d6e, 189000, 1, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0x6038c459cb274a3aadcc1fed6ae32f98);

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `id` bigint(20) NOT NULL,
  `amount` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `expire_at` datetime(6) DEFAULT NULL,
  `gateway` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `fee_ship` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`id`, `amount`, `created_at`, `expire_at`, `gateway`, `status`, `type`, `updated_at`, `fee_ship`) VALUES
(1, 174000, '2025-04-13 16:20:55.000000', '2025-04-13 16:22:55.000000', NULL, 0, 1, NULL, 0),
(2, 174000, '2025-04-13 16:22:26.000000', '2025-04-13 16:24:26.000000', NULL, 0, 1, NULL, 0),
(3, 174000, '2025-04-13 16:28:36.000000', '2025-04-13 16:30:36.000000', NULL, 0, 1, NULL, 0),
(4, 899000, '2025-04-16 23:05:36.000000', '2025-04-16 23:07:36.000000', NULL, 0, 1, NULL, 0),
(5, 199000, '2025-04-16 23:08:43.000000', '2025-04-16 23:10:43.000000', NULL, 0, 1, NULL, 0),
(6, 319000, '2025-04-17 01:24:42.000000', '2025-04-17 01:26:42.000000', NULL, 0, 1, NULL, 0),
(7, 386502, '2025-04-17 18:18:13.000000', '2025-04-17 18:20:13.000000', NULL, 0, 1, NULL, 0),
(8, 191502, '2025-04-17 18:21:20.000000', NULL, NULL, 1, 0, NULL, 0),
(9, 370502, '2025-04-17 21:49:55.000000', NULL, NULL, 1, 0, NULL, 0),
(10, 416502, '2025-04-17 22:20:57.000000', NULL, NULL, 1, 0, NULL, 0),
(11, 261502, '2025-04-17 22:31:48.000000', NULL, NULL, 1, 0, NULL, 0),
(12, 291502, '2025-04-17 22:37:28.000000', NULL, NULL, 1, 0, NULL, 0),
(13, 350502, '2025-04-17 22:39:53.000000', NULL, NULL, 1, 0, NULL, 0),
(14, 416502, '2025-04-17 22:47:05.000000', NULL, NULL, 1, 0, NULL, 0),
(15, 201502, '2025-04-17 22:53:10.000000', NULL, NULL, 1, 0, NULL, 0),
(16, 201502, '2025-04-17 22:56:41.000000', NULL, NULL, 1, 0, NULL, 0),
(17, 536502, '2025-04-17 23:03:17.000000', NULL, NULL, 1, 0, NULL, 0),
(18, 319997, '2025-04-17 23:08:01.000000', NULL, NULL, 1, 0, NULL, 0),
(19, 278997, '2025-04-17 23:09:12.000000', NULL, NULL, 1, 0, NULL, 0),
(20, 251502, '2025-04-18 13:03:36.000000', NULL, NULL, 1, 0, NULL, 0),
(21, 123997, '2025-04-18 13:08:27.000000', '2025-04-18 13:10:27.000000', NULL, 0, 1, NULL, 0),
(22, 164001, '2025-04-18 13:12:01.000000', '2025-04-18 13:14:01.000000', NULL, 0, 1, NULL, 0),
(23, 156502, '2025-04-18 13:23:26.000000', '2025-04-18 13:25:26.000000', NULL, 2, 1, NULL, 0),
(24, 121502, '2025-04-18 13:40:07.000000', '2025-04-18 13:42:07.000000', NULL, 0, 1, NULL, 0),
(25, 191503, '2025-04-18 15:51:07.000000', NULL, NULL, 1, 0, NULL, 0),
(26, 176503, '2025-04-18 16:43:47.000000', NULL, NULL, 1, 0, NULL, 0),
(27, 255502, '2025-04-18 16:49:12.000000', NULL, NULL, 1, 0, NULL, 0),
(28, 281001, '2025-04-18 16:50:33.000000', NULL, NULL, 1, 0, NULL, 0),
(29, 181001, '2025-04-18 23:41:24.000000', NULL, NULL, 1, 0, NULL, 0),
(30, 391001, '2025-04-19 10:27:02.000000', NULL, NULL, 4, 0, NULL, 26001),
(31, 286502, '2025-04-19 10:59:45.000000', NULL, NULL, 1, 0, NULL, 51502),
(32, 181502, '2025-04-19 12:14:31.000000', '2025-04-19 12:16:31.000000', NULL, 0, 1, NULL, 51502),
(33, 276502, '2025-04-19 17:01:14.000000', NULL, NULL, 1, 0, NULL, 51502),
(34, 255502, '2025-04-19 17:02:52.000000', '2025-04-19 17:04:52.000000', NULL, 0, 1, NULL, 51502),
(35, 121502, '2025-04-19 17:23:13.000000', '2025-04-19 17:25:13.000000', NULL, 0, 1, NULL, 51502),
(36, 181502, '2025-04-19 17:28:46.000000', '2025-04-19 17:30:46.000000', NULL, 0, 1, NULL, 51502),
(37, 213997, '2025-04-20 14:32:21.000000', NULL, NULL, 1, 0, NULL, 73997),
(38, 213997, '2025-04-20 14:32:40.000000', '2025-04-20 14:34:40.000000', NULL, 0, 1, NULL, 73997),
(39, 161997, '2025-04-20 14:44:11.000000', '2025-04-20 14:46:11.000000', NULL, 0, 1, NULL, 73997),
(40, 161997, '2025-04-20 14:46:47.000000', '2025-04-20 14:48:47.000000', NULL, 0, 1, NULL, 73997),
(41, 161997, '2025-04-20 14:48:48.000000', '2025-04-20 14:50:48.000000', NULL, 2, 1, NULL, 73997),
(42, 161997, '2025-04-20 14:50:33.000000', '2025-04-20 14:52:33.000000', NULL, 0, 1, NULL, 73997),
(43, 161997, '2025-04-20 14:54:27.000000', '2025-04-20 14:56:27.000000', NULL, 2, 1, NULL, 73997),
(44, 161997, '2025-04-20 15:03:36.000000', '2025-04-20 15:05:36.000000', NULL, 2, 1, NULL, 73997),
(45, 161997, '2025-04-20 15:07:03.000000', '2025-04-20 15:09:03.000000', NULL, 0, 1, NULL, 73997),
(46, 231502, '2025-04-20 21:02:52.000000', '2025-04-20 21:04:52.000000', NULL, 0, 1, NULL, 51502),
(47, 276502, '2025-04-20 21:25:24.000000', '2025-04-20 21:27:24.000000', NULL, 2, 1, NULL, 51502),
(48, 255502, '2025-04-20 21:35:24.000000', '2025-04-20 21:37:24.000000', NULL, 7, 1, NULL, 51502),
(49, 255502, '2025-04-20 21:43:09.000000', '2025-04-20 21:45:09.000000', NULL, 6, 1, NULL, 51502),
(50, 161997, '2025-04-21 11:58:51.000000', '2025-04-21 12:00:51.000000', NULL, 0, 1, NULL, 73997),
(51, 161997, '2025-04-21 12:03:17.000000', '2025-04-21 12:05:17.000000', NULL, 0, 1, NULL, 73997),
(52, 161997, '2025-04-21 12:08:28.000000', '2025-04-21 12:10:28.000000', NULL, 0, 1, NULL, 73997),
(53, 286502, '2025-04-21 12:34:20.000000', '2025-04-21 12:36:20.000000', NULL, 0, 1, NULL, 51502),
(54, 255502, '2025-04-21 12:39:11.000000', '2025-04-21 12:41:11.000000', NULL, 0, 1, NULL, 51502),
(55, 401502, '2025-04-21 12:51:42.000000', '2025-04-21 12:53:42.000000', NULL, 0, 1, NULL, 51502),
(56, 336502, '2025-04-21 12:54:16.000000', '2025-04-21 12:56:16.000000', NULL, 2, 1, NULL, 51502),
(57, 196502, '2025-04-21 13:15:38.000000', '2025-04-21 13:17:38.000000', NULL, 2, 1, NULL, 51502),
(58, 206502, '2025-04-21 13:20:10.000000', '2025-04-21 13:22:10.000000', NULL, 2, 1, NULL, 51502),
(59, 255502, '2025-04-21 13:24:38.000000', '2025-04-21 13:26:38.000000', NULL, 2, 1, NULL, 51502),
(60, 191502, '2025-04-21 21:13:04.000000', '2025-04-21 21:15:04.000000', NULL, 0, 1, NULL, 51502),
(61, 255502, '2025-04-21 21:15:06.000000', '2025-04-21 21:17:06.000000', NULL, 2, 1, NULL, 51502),
(62, 186502, '2025-04-21 21:19:04.000000', '2025-04-21 21:21:04.000000', NULL, 2, 1, NULL, 51502),
(63, 276502, '2025-04-21 21:33:06.000000', '2025-04-21 21:35:06.000000', NULL, 2, 1, NULL, 51502),
(64, 255502, '2025-04-21 21:37:20.000000', '2025-04-21 21:39:20.000000', NULL, 2, 1, NULL, 51502),
(65, 360502, '2025-04-22 08:34:44.000000', '2025-04-22 08:36:44.000000', NULL, 0, 1, NULL, 51502),
(66, 251001, '2025-04-22 09:33:18.000000', NULL, NULL, 1, 0, NULL, 26001),
(67, 178997, '2025-05-03 22:17:38.000000', NULL, NULL, 1, 0, NULL, 63997),
(68, 251502, '2025-05-03 22:24:42.000000', NULL, NULL, 1, 0, NULL, 51502),
(69, 286502, '2025-05-03 22:37:24.000000', NULL, NULL, 4, 0, NULL, 51502),
(70, 146502, '2025-05-03 22:40:39.000000', NULL, NULL, 4, 0, NULL, 51502),
(71, 526502, '2025-05-04 11:14:29.000000', NULL, NULL, 4, 0, NULL, 51502),
(72, 126502, '2025-05-05 13:46:21.000000', NULL, NULL, 4, 0, NULL, 51502),
(73, 245502, '2025-05-05 14:03:40.000000', '2025-05-05 14:05:40.000000', NULL, 4, 1, NULL, 51502),
(74, 151502, '2025-05-05 15:24:10.000000', NULL, NULL, 7, 0, NULL, 51502),
(75, 150502, '2025-05-05 21:05:56.000000', '2025-05-05 21:07:56.000000', NULL, 2, 1, NULL, 51502),
(76, 271502, '2025-05-05 21:35:58.000000', NULL, NULL, 4, 0, NULL, 51502),
(77, 218502, '2025-05-05 21:45:32.000000', NULL, NULL, 4, 0, NULL, 51502),
(78, 126502, '2025-05-05 21:48:07.000000', NULL, NULL, 4, 0, NULL, 51502),
(79, 146502, '2025-05-05 21:54:03.000000', NULL, NULL, 4, 0, NULL, 51502),
(80, 171502, '2025-05-05 22:02:00.000000', NULL, NULL, 4, 0, NULL, 51502),
(81, 149502, '2025-05-05 22:09:36.000000', NULL, NULL, 4, 0, NULL, 51502),
(82, 116502, '2025-05-05 22:12:26.000000', NULL, NULL, 4, 0, NULL, 51502),
(83, 126502, '2025-05-05 22:18:12.000000', NULL, NULL, 4, 0, NULL, 51502),
(84, 336502, '2025-05-06 08:28:03.000000', NULL, NULL, 1, 0, NULL, 51502),
(85, 218502, '2025-05-06 08:39:40.000000', NULL, NULL, 7, 0, NULL, 51502),
(86, 259502, '2025-05-06 08:41:54.000000', NULL, NULL, 6, 0, NULL, 51502),
(87, 234502, '2025-05-06 08:44:31.000000', NULL, NULL, 7, 0, NULL, 51502),
(88, 194502, '2025-05-06 08:46:49.000000', NULL, NULL, 7, 0, NULL, 51502),
(89, 116502, '2025-05-06 11:17:04.000000', '2025-05-06 11:19:04.000000', NULL, 0, 1, NULL, 51502),
(90, 194502, '2025-05-06 14:49:25.000000', '2025-05-06 14:51:25.000000', NULL, 0, 1, NULL, 51502),
(91, 156502, '2025-05-06 14:54:38.000000', '2025-05-06 14:56:38.000000', NULL, 0, 1, NULL, 51502),
(92, 218502, '2025-05-06 14:56:52.000000', '2025-05-06 14:58:52.000000', NULL, 0, 1, NULL, 51502),
(93, 149502, '2025-05-06 18:01:10.000000', '2025-05-06 18:03:10.000000', NULL, 7, 1, NULL, 51502),
(94, 139502, '2025-05-06 18:08:29.000000', '2025-05-06 18:10:29.000000', NULL, 6, 1, NULL, 51502),
(95, 146502, '2025-05-06 18:10:00.000000', '2025-05-06 18:12:00.000000', NULL, 0, 1, NULL, 51502),
(96, 143502, '2025-05-06 18:11:17.000000', '2025-05-06 18:13:17.000000', NULL, 6, 1, NULL, 51502),
(97, 116502, '2025-05-06 18:16:00.000000', '2025-05-06 18:18:00.000000', NULL, 6, 1, NULL, 51502),
(98, 143502, '2025-05-06 18:19:51.000000', NULL, NULL, 6, 0, NULL, 51502),
(99, 370502, '2025-05-20 11:17:22.000000', '2025-05-20 11:19:22.000000', 0, 4, 1, NULL, 51502),
(100, 240502, '2025-05-20 20:31:06.000000', NULL, 1, 1, 0, NULL, 51502),
(101, 430502, '2025-05-24 22:07:19.000000', NULL, 1, 1, 0, NULL, 51502),
(102, 391502, '2025-05-25 23:08:19.000000', NULL, 1, 1, 0, NULL, 51502),
(103, 370502, '2025-05-27 22:03:34.000000', NULL, 1, 6, 0, NULL, 51502),
(104, 266502, '2025-05-27 22:21:09.000000', NULL, 1, 6, 0, NULL, 51502),
(105, 286502, '2025-05-27 22:23:37.000000', NULL, 1, 6, 0, NULL, 51502),
(106, 266502, '2025-05-28 17:54:08.000000', NULL, 1, 6, 0, NULL, 51502),
(107, 186502, '2025-05-28 18:04:05.000000', NULL, 1, 6, 0, NULL, 51502),
(108, 391502, '2025-05-28 18:06:37.000000', NULL, 1, 1, 0, NULL, 51502),
(109, 251502, '2025-05-28 22:34:56.000000', NULL, 1, 6, 0, NULL, 51502),
(110, 360502, '2025-05-28 23:47:56.000000', NULL, 1, 4, 0, NULL, 51502),
(111, 360502, '2025-05-28 23:52:12.000000', NULL, 1, 4, 0, NULL, 51502),
(112, 240502, '2025-05-28 23:53:12.000000', NULL, 1, 4, 0, NULL, 51502),
(113, 181502, '2025-05-28 23:54:52.000000', NULL, 1, 4, 0, NULL, 51502),
(114, 150502, '2025-05-28 23:56:06.000000', NULL, 1, 4, 0, NULL, 51502),
(115, 565502, '2025-05-30 10:24:01.000000', NULL, 1, 6, 0, NULL, 51502),
(116, 291502, '2025-05-31 23:23:55.000000', NULL, 1, 6, 0, NULL, 51502),
(117, 181502, '2025-06-01 21:52:28.000000', NULL, 1, 1, 0, NULL, 51502),
(118, 240502, '2025-06-01 23:10:08.000000', NULL, 1, 4, 0, NULL, 51502),
(120, 184001, '2025-06-02 21:32:24.000000', NULL, 1, 5, 0, NULL, 34001),
(121, 605502, '2025-06-08 22:40:04.000000', NULL, 1, 6, 0, NULL, 51502);

-- --------------------------------------------------------

--
-- Table structure for table `provinces`
--

CREATE TABLE `provinces` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `provinces`
--

INSERT INTO `provinces` (`id`, `name`) VALUES
(201, 'Hà Nội'),
(202, 'Hồ Chí Minh'),
(231, 'Nam Định'),
(244, 'Thái Nguyên'),
(247, 'Lạng Sơn'),
(248, 'Bắc Giang'),
(253, 'Bạc Liêu'),
(258, 'Bình Thuận'),
(259, 'Kon Tum'),
(264, 'Lai Châu'),
(265, 'Điện Biên'),
(266, 'Sơn La'),
(267, 'Hòa Bình'),
(268, 'Hưng Yên'),
(269, 'Lào Cai');

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

CREATE TABLE `review` (
  `id` binary(16) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `rate_point` int(11) DEFAULT NULL,
  `book_id` binary(16) DEFAULT NULL,
  `user_id` binary(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `review`
--

INSERT INTO `review` (`id`, `content`, `created_at`, `rate_point`, `book_id`, `user_id`) VALUES
(0x0437dabd7c9446aeadbf1a6435e47f44, 'Tuyệt vời, rất thú vị', '2025-05-28', 4, 0x1f79e2f9184811f09d2700155d851915, 0xa180561dbf4349e080760c5c466136c0),
(0x0d2f499e882b447680998aa3ffc06d8b, 'Tệ nhưng không đến nỗi ', '2025-05-28', 2, 0x1f79e4d6184811f09d2700155d851915, 0xa180561dbf4349e080760c5c466136c0),
(0x61e34b6039044765a2f267d7c4dd1988, 'Hay', '2025-05-28', 5, 0x1f79f89b184811f09d2700155d851915, 0xa180561dbf4349e080760c5c466136c0),
(0x6726b1e6fd694eadaa9cb698ab3191a2, 'Nói chung là hay và tuyệt vời', '2025-05-28', 5, 0x1f79f9b2184811f09d2700155d851915, 0xa180561dbf4349e080760c5c466136c0),
(0x6af1ce9f84ba4208b66d9dae8de3aae6, 'Cuốn sách rất hay và bổ ích, rất phù hợp với những bạn đam mê về vũ trụ\n', '2025-05-30', 5, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0x7881214a8b60416dbd02810e4221ec34, 'Cuốn sách rất hay, thay đổi tư duy cho những người kinh doanh', '2025-05-28', 4, 0x1f7a0060184811f09d2700155d851915, 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0x894e0a3ff7994296ad1af35e02dd1550, 'hay', '2025-05-30', 5, 0x1f79e4d6184811f09d2700155d851915, 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0x8a3e7a918fd146f09e3aef91c998b96e, 'Rất ý nghĩa', '2025-05-30', 5, 0xd10ca66dd1f648899738346b2cb1a653, 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0x8c7dbb91f1d244da908fce1600b2fb70, 'Quyển sách thực sự có ý nghĩa với mình, mở ra 1 cái nhìn khác của mình trong vũ trụ', '2025-05-31', 4, 0x1f79b19b184811f09d2700155d851915, 0xb83d3d4a9e6f45ee9140ffa8dea3d8bd),
(0x9f0d318b40844ea9b84b9bf08820219c, 'Cực kì hay đối với tôi', '2025-05-28', 5, 0x1f7a0146184811f09d2700155d851915, 0xb653efc8f9fe4ca880bcbc63ae82418b),
(0xc670dfc8d6e641a5bcc1fdae40c56738, 'Phải gọi là một trong những cuốn sách cực kì hay và hấp dẫn', '2025-05-28', 5, 0x18818805b39b479dbfbe31b0ed5b7f2b, 0xa180561dbf4349e080760c5c466136c0),
(0xc9eee4c571d54174af490ac2fac74b2d, 'Cuốn sách rất hay và ý nghĩa', '2025-05-28', 5, 0x1f7a0968184811f09d2700155d851915, 0xa180561dbf4349e080760c5c466136c0),
(0xd8e39eba7db848d3962d61f28aa74c83, 'Cuốn sách rất hay và thú vị\n', '2025-05-28', 5, 0x1f79b19b184811f09d2700155d851915, 0xa180561dbf4349e080760c5c466136c0),
(0xe9999487de214209ae6cad3421caae82, 'Cuốn sách cực kì bổ ích cho những bà mẹ nội trợ', '2025-05-28', 5, 0x1f7a084c184811f09d2700155d851915, 0xa180561dbf4349e080760c5c466136c0);

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `role_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `role_name`) VALUES
(1, 'ADMIN'),
(2, 'USER');

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

CREATE TABLE `room` (
  `id` bigint(20) NOT NULL,
  `chatroom_id` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `user_id` binary(16) DEFAULT NULL,
  `room_id` varchar(255) DEFAULT NULL,
  `user_avatar` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`id`, `chatroom_id`, `created_at`, `user_id`, `room_id`, `user_avatar`, `user_name`) VALUES
(7, NULL, '2025-05-30 22:07:07.000000', 0xb653efc8f9fe4ca880bcbc63ae82418b, 'room_b653efc8-f9fe-4ca8-80bc-bc63ae82418b', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748446330/minhquang/avatar.jpg_20250528223207.png', 'quangminh1'),
(8, NULL, '2025-05-30 23:31:14.000000', 0xa180561dbf4349e080760c5c466136c0, 'room_a180561d-bf43-49e0-8076-0c5c466136c0', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748357674/minhquang/avatar.jpg_20250527215426.png', 'quangminh'),
(11, NULL, '2025-06-02 13:58:11.000000', 0x26ce5564f32a4125b8cad3c89f80466c, 'room_26ce5564-f32a-4125-b8ca-d3c89f80466c', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748847487/minhquang/avatar.jpg_20250602135802.png', 'hoacute'),
(13, NULL, '2025-06-08 21:00:24.000000', 0x31be4722e311440eb571c33a65c3992a, 'room_31be4722-e311-440e-b571-c33a65c3992a', 'https://res.cloudinary.com/dmotq51fh/image/upload/v1749391216/minhquang/avatar.jpg_20250608210010.png', 'minhminh03');

-- --------------------------------------------------------

--
-- Table structure for table `token_invalid`
--

CREATE TABLE `token_invalid` (
  `id` binary(16) NOT NULL,
  `expires` datetime(6) DEFAULT NULL,
  `token` varchar(1024) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `token_invalid`
--

INSERT INTO `token_invalid` (`id`, `expires`, `token`) VALUES
(0x4178720450c54fd989662291214da31b, '2025-04-21 14:39:36.000000', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxdWFuZzMiLCJpZCI6IjQxNzg3MjA0LTUwYzUtNGZkOS04OTY2LTIyOTEyMTRkYTMxYiIsImlhdCI6MTc0NDYxNjM3NiwiZXhwIjoxNzQ1MjIxMTc2LCJ0eXBlIjoicmVmcmVzaCJ9.zy9wLNxvipqQYGp00SIsut7yhMW_l3_qet05U8WH0XE2MQLcZ-g8wgjuQPc6mdM5EVk2jZphSi6i67QEesAqiw'),
(0x4a4ee5e3d9554301a46bdba6b400d246, '2025-04-21 14:37:36.000000', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxdWFuZzMiLCJpZCI6IjRhNGVlNWUzLWQ5NTUtNDMwMS1hNDZiLWRiYTZiNDAwZDI0NiIsImlhdCI6MTc0NDYxNjI1NiwiZXhwIjoxNzQ1MjIxMDU2LCJ0eXBlIjoicmVmcmVzaCJ9.y8yk-jbEc10G2ArkHdCCLAvWStQMbwvRdbuE9QOyXKRMhcDgEAwXTtHJ_gn4qTSJNT4rKFNHuo6TRgsRe1zxAg'),
(0xb2a2388dfd484f629f2f561ba2593407, '2025-04-21 13:50:11.000000', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxdWFuZzMiLCJpZCI6ImIyYTIzODhkLWZkNDgtNGY2Mi05ZjJmLTU2MWJhMjU5MzQwNyIsImlhdCI6MTc0NDYxMzQxMSwiZXhwIjoxNzQ1MjE4MjExLCJ0eXBlIjoicmVmcmVzaCJ9.U7UOwnSBZWLClaE5tC1vSnku98P32fcZ0_loGMG5vXgBA5Al3pX5IU49EEBwVSwsVYufNnY59es99fwHy8DjJA'),
(0xb3b726f53ed74991b04f414ff1e91a97, '2025-04-21 14:40:28.000000', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlkIjoiYjNiNzI2ZjUtM2VkNy00OTkxLWIwNGYtNDE0ZmYxZTkxYTk3IiwiaWF0IjoxNzQ0NjE2NDI4LCJleHAiOjE3NDUyMjEyMjgsInR5cGUiOiJyZWZyZXNoIn0.k0-oLlVEsg_L8KhkbUL3B8lnOLWXmG9MpHnboLdHFEJ7cnw8ntxDB0tfyY3o88B8MLl2rWC7_j2lEUEltjNZvg');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` binary(16) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `device_token` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(20) DEFAULT NULL,
  `last_name` varchar(20) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(10) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `avatar`, `device_token`, `email`, `first_name`, `last_name`, `password`, `phone_number`, `username`) VALUES
(0x14983eea2fd74edd8ddf146c7927ed23, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748622608/minhquang/avatar.jpg_20250530233004.png', NULL, NULL, NULL, NULL, '$2a$10$KtafRY0URNMonVew89fOAOk3P6NpxgZBTsf.1EInn89WgbeYA6r8q', NULL, 'admin'),
(0x15e425bbeae3476e970b4afc0a1f5932, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748795894/minhquang/avatar.jpg_20250601233812.jpg', NULL, 'quangquang@gmail.com', NULL, NULL, '$2a$10$RSlyOnxTiNvVCUGebT6OQ.EhVBwhG.8Okrj1NQ5Hso67ocAG7WrCq', NULL, 'quangsixgai'),
(0x18dd608303824f3fb5b8fde4dec994b9, NULL, NULL, NULL, NULL, NULL, '$2a$10$1NgMmVDP88lYXrY41ZZ5H.bswr6br6BR4g0QaBvAPwAOceyT.quuq', NULL, 'user'),
(0x20fdbd3310754cc2af1ad8213978fb7f, NULL, NULL, 'quang7@gmail.com', NULL, NULL, '$2a$10$uNoeLRtD9MXoxI..bhp6FOOXsnvr4pPPbdCoDb6eB4C64wFxLt1C6', NULL, 'quang7'),
(0x21ae05238118489a86cf7edf55d31901, NULL, NULL, 'tienlarac611@gmail.com', NULL, NULL, '$2a$10$B00xk4EL9ePWacYZRNr0ruHcBneuzacwriSHoG9wTiZfsEKr4OeSO', NULL, 'quang01'),
(0x26ce5564f32a4125b8cad3c89f80466c, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748847487/minhquang/avatar.jpg_20250602135802.png', NULL, 'hoact@gmail.com', NULL, NULL, '$2a$10$BUrD.pn6EjCsTkTXuOX9Sucx1ni7vM2ZhuEQ7q3Z.TMNjP4vBmtqS', NULL, 'hoacute'),
(0x31be4722e311440eb571c33a65c3992a, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1749391216/minhquang/avatar.jpg_20250608210010.png', NULL, 'minhminh@gmail.com', NULL, NULL, '$2a$10$mdTE8Jjw2DPuxuQg0jssbeYkO5/5LBzscDgoHNPV0sI741tEt/j1.', NULL, 'minhminh03'),
(0x4e916c2f24624a0eb1322ad0699a26c3, NULL, NULL, 'quang9quang@gmail.com', NULL, NULL, '$2a$10$k8zQWfQ2jm.aqNbcGaTFPuyEwBhnuZMuhJxiTLNByzHrBjpEd.yO6', NULL, 'quang99'),
(0x78e6465a87da45bcb30c0072453d8ce1, NULL, NULL, 'quangquang29@gmail.com', NULL, NULL, '$2a$10$FRLMK7V3fo2cPhtFgjzz/eUhZWj81eQj/qtfyQ3zeST0QdURFnUSe', NULL, 'quangquang'),
(0x7aea20848d0841ddb3575d9f242c3b9f, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748357395/minhquang/lordofthering.jpg_20250527214952.jpg', NULL, 'quang2999@gmail.com', 'Minh', 'Quang', '$2a$10$B5AxI4.oNw395I0JvXYpVuRKCgc11J7Xu2lvPla6YkcDNs.l.RERS', '0944711631', 'quang3'),
(0x8a1a64cdcaa442b98c3d37195bba136f, NULL, NULL, 'quangquang299@gmail.com', NULL, NULL, '$2a$10$mD3H44KxY1fxQSKjfrZz4uvfayjw39CTbtGZnLrH0u12xLWugHlye', NULL, 'quangquang29'),
(0xa180561dbf4349e080760c5c466136c0, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748623510/minhquang/avatar.jpg_20250530234506.jpg', NULL, 'quangminh@gmail.com', 'Quang', 'Minh Nguyen', '$2a$10$Xcdd6pzsEc12HQyWb7Cxp.pf2KeEX7JczSICaQbiLh9oumCsdq5QK', '0338032604', 'quangminh'),
(0xab87e54994dd4c04883db71141eeedbc, NULL, NULL, 'quangquang12@gmail.com', NULL, NULL, '$2a$10$wR03DAXaFxrhXLeH2lhCze.VaxAkgwrImBJPC80dhHEo5motAY5zy', NULL, 'quang12'),
(0xacd565dd760a453b844857cc1602d0ae, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748710482/minhquang/avatar.jpg_20250531235438.jpg', NULL, 'quang2990@gmail.com', NULL, NULL, '$2a$10$JamJnguzGOYsTormbzbbNuYac8.ouwovOMDCuBZoiYwSc2WSAl/zS', NULL, 'quanghihi'),
(0xb231fe240f1a47dcacaaf2084002431e, NULL, NULL, 'quang4@gmail.com', NULL, NULL, '$2a$10$iQe4YLKjSVSKvxBlD.AmhuaJxa1ytjUb.WAjLRneU7g3waKn5PFla', NULL, 'quang4'),
(0xb653efc8f9fe4ca880bcbc63ae82418b, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748446330/minhquang/avatar.jpg_20250528223207.png', NULL, 'quangminh1@gmail.com', 'Nguyen', 'Quang', '$2a$10$OUDnp6jq5z0u1Qt.QWtISeT2wc2GF0t3aF3AuRxJlPguq1hKa.j2K', '0987541587', 'quangminh1'),
(0xb83d3d4a9e6f45ee9140ffa8dea3d8bd, 'https://res.cloudinary.com/dmotq51fh/image/upload/v1748700723/minhquang/avatar.jpg_20250531211158.png', NULL, 'linhnt@gmail.com', NULL, NULL, '$2a$10$76iOGl269n/AG0OTVUWzcuv3zL2joA5cCooRW3pY1/KTYBfEoCf/O', NULL, 'linhxinhgai'),
(0xcc41b62078ec4f8cb128bc21d2b8fc36, NULL, NULL, 'quang123@gmail.com', NULL, NULL, '$2a$10$qbJwts6rNQuQiVfagyh10.WACmSib4oE1FxsKLLhVufSrpjPk0wbe', NULL, 'quang02'),
(0xd17598bdd6d0482b867c38b82d35e56c, NULL, NULL, 'vquangn2990@gmail.com', NULL, NULL, '$2a$10$WuC4R4BfKJOEsJpPay7HBeRYxgwtaRwt36Hv1p9wTMgUMg/oSnDkO', NULL, 'quang9'),
(0xf10bf57d57384d0f94bf3e0b91bd99d1, NULL, NULL, 'quang1@gmail.com', NULL, NULL, '$2a$10$rt.eIrhOegix2q.lSR2VXOGeDuCMYEL26xbN4sd3cFfUblvhY53fW', NULL, 'quang1'),
(0xf207b218a13b4bc8aff035b41a5f2f33, NULL, NULL, 'quangqq@gmail.com', NULL, NULL, '$2a$10$s3ueww5F/.itNMCwqpzMAOdHVwsXwF3wAV8LR3gNayXaHf739c6Gu', NULL, 'quangqq');

-- --------------------------------------------------------

--
-- Table structure for table `users_liked_books`
--

CREATE TABLE `users_liked_books` (
  `user_id` binary(16) NOT NULL,
  `book_id` binary(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users_liked_books`
--

INSERT INTO `users_liked_books` (`user_id`, `book_id`) VALUES
(0x4e916c2f24624a0eb1322ad0699a26c3, 0x1f79e2f9184811f09d2700155d851915),
(0x4e916c2f24624a0eb1322ad0699a26c3, 0x1f79e5e3184811f09d2700155d851915),
(0x4e916c2f24624a0eb1322ad0699a26c3, 0x1f79f426184811f09d2700155d851915),
(0x7aea20848d0841ddb3575d9f242c3b9f, 0x1f79e2f9184811f09d2700155d851915),
(0x7aea20848d0841ddb3575d9f242c3b9f, 0x1f79e5e3184811f09d2700155d851915),
(0x7aea20848d0841ddb3575d9f242c3b9f, 0x1f79f426184811f09d2700155d851915),
(0x7aea20848d0841ddb3575d9f242c3b9f, 0x1f79f6b0184811f09d2700155d851915),
(0x7aea20848d0841ddb3575d9f242c3b9f, 0x1f79f7a5184811f09d2700155d851915),
(0x7aea20848d0841ddb3575d9f242c3b9f, 0x1f79f89b184811f09d2700155d851915),
(0x7aea20848d0841ddb3575d9f242c3b9f, 0x1f79f9b2184811f09d2700155d851915),
(0x7aea20848d0841ddb3575d9f242c3b9f, 0x1f79faa3184811f09d2700155d851915),
(0xa180561dbf4349e080760c5c466136c0, 0x18818805b39b479dbfbe31b0ed5b7f2b),
(0xa180561dbf4349e080760c5c466136c0, 0x1f79b19b184811f09d2700155d851915),
(0xb653efc8f9fe4ca880bcbc63ae82418b, 0x1f79b19b184811f09d2700155d851915),
(0xb653efc8f9fe4ca880bcbc63ae82418b, 0x1f79e2f9184811f09d2700155d851915),
(0xb653efc8f9fe4ca880bcbc63ae82418b, 0x1f79e4d6184811f09d2700155d851915),
(0xb653efc8f9fe4ca880bcbc63ae82418b, 0x1f79f6b0184811f09d2700155d851915),
(0xb653efc8f9fe4ca880bcbc63ae82418b, 0x1f79f7a5184811f09d2700155d851915),
(0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x1f79e5e3184811f09d2700155d851915),
(0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x1f79f5af184811f09d2700155d851915),
(0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x1f79f7a5184811f09d2700155d851915),
(0xf10bf57d57384d0f94bf3e0b91bd99d1, 0x1f79f9b2184811f09d2700155d851915);

-- --------------------------------------------------------

--
-- Table structure for table `user_address`
--

CREATE TABLE `user_address` (
  `id` binary(16) NOT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `is_primary` bit(1) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `receiver_name` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `district_id` int(11) DEFAULT NULL,
  `province_id` int(11) DEFAULT NULL,
  `ward_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_address`
--

INSERT INTO `user_address` (`id`, `detail`, `is_primary`, `phone_number`, `receiver_name`, `username`, `district_id`, `province_id`, `ward_id`) VALUES
(0x064338789eae4291ad71af4af57a3934, '', b'0', '0965711445', 'Quan', 'quang1', 2205, 259, '360508'),
(0x0b9d35991fdb4276850958f2e3eaa532, 'số nhà 12', b'0', '09563421552', 'Nguyen Van A', 'quang1', 3311, 247, '800080'),
(0x0dacc2fc8d944f98a45f9d79ff11910c, 'Số nhà 11 ngõ 166 Tân Triều', b'1', '0944711632', 'Nguyễn Thị Hoa', 'hoacute', 1710, 201, '1A1111'),
(0x0ed99e635e25445e96ce2ead532d2e6c, 'xom 4 Ninh My', b'0', '0944711632', 'Nguyen Minh Quang', 'quang1', 1840, 231, '251012'),
(0x19f7bb51280f440b91e8e451679f07f4, 'Xóm 4 Ninh Mỹ', b'1', '0338032604', 'Nguyễn Thành Công', 'quangminh1', 1840, 231, '251012'),
(0x1b4dc2a612444bc48358e5cb2ab4e9b0, '', b'0', '0336032604', 'Nguyen Thanh Dien', 'quang3', 2157, 267, '230915'),
(0x2481decaca82455b8d5e3e49a8e64e68, '', b'0', '0336032604', 'Nguyen Thanh Dien', 'quang3', 1777, 258, '470416'),
(0x24baed2d054e449cb999aaf9a294bf80, '', b'0', '0944711632', 'Nguyen Minh Quang', 'quang1', 1442, 202, '20102'),
(0x3324a5661e6b4af385022c9ca7401d47, 'Số nhà 20', b'0', '0944711632', 'Nguyen Minh Quang', 'quang7', 1723, 253, '600704'),
(0x350da00b2de442ac83497d9ac47e4310, '', b'0', '0959435435', 'Công', 'quang1', 1989, 264, '70314'),
(0x40a1a6c5dca94b7e9066229c59d6453c, 'Số nhà 20', b'1', '0944711632', 'Nguyễn Minh Quang', 'quang99', 1840, 231, '251012'),
(0x4651bc1b184811f09d2700155d851915, '144 Chiến Thắng', b'0', '0944711632', 'Nguyễn Minh Quang', 'quang3', 1710, 201, '1A1111'),
(0x4655c566184811f09d2700155d851915, 'Xóm 4 Ninh Mỹ', b'0', '0944711632', 'Nguyễn Minh Quang', 'quang3', 1840, 231, '251012'),
(0x4b4f000877364f33b8246f0c0b481e70, 'Số nhà 30', b'0', '0944711632', 'Nguyễn Minh Quân', 'quang3', 2156, 267, '230521'),
(0x50ecb90c2078429497dd2ddf9cf56f6e, 'Số nhà 20', b'1', '0944711632', 'Nguyễn Minh Quang', 'quangquang', 2045, 268, '221001'),
(0x50fde11137f84f3ebf65964da2e4864b, 'số nhà 98, ngõ 322', b'1', '0334781448', 'Vu Van Dung', 'quang1', 3440, 201, '13004'),
(0x553e3a7aad0045f389bbc4048e164878, '', b'0', 'dsafdsafadsf', 'dsafsdfsa', 'quang3', 2264, 269, '80213'),
(0x648f1b47539346eba8ce9cc0fbc4bf19, 'dfsg', b'0', 'dfgsdg', 'fgdg', 'quang1', 2270, 267, '231012'),
(0x66bdae0cdde14ad892d912c141c8c11d, 'số nhà 12', b'0', '09563421552', 'Nguyen Van A', 'quang1', 3311, 247, '800080'),
(0x70920b76012142d2b79f6ebd7a744eac, 'số nhà 12', b'0', '0944711632', 'Nguyên Minh Quang', 'quang1', 1924, 244, '120321'),
(0x7153fdb1e52c45929fd307464e8627eb, 'Số nhà 20', b'0', '0944711632', 'Nguyễn Thị Hương', 'quang3', 2045, 268, '221011'),
(0x71c8d316b3034c67a60e28c867ed50e0, 'Xóm 4 Ninh Mỹ', b'1', '0944711632', 'Nguyễn Minh Quang', 'quang12', 1840, 231, '251012'),
(0x7936adf839714b7eb53840c8fceca645, 'xóm 4 Mỹ Đức', b'0', '0965971443', 'Nguyen Thi Linh', 'quang1', 1840, 231, '251012'),
(0x808bd71de0e1424f9b08e517b094ea5c, 'số nhà 12', b'0', '09563421552', 'Nguyen Van A', 'quang1', 3311, 247, '800080'),
(0x95f9bdf526004eb68146f3095da2123c, 'số nhà 12', b'0', '09563421552', 'Nguyen Van A', 'quang1', 3311, 247, '800080'),
(0xa0b56a801b0d400eae58b148e3029b59, 'số nhà 11', b'0', '08234273424', 'Nguyen Thi Thu Huyen', 'quang3', 3230, 266, '140315'),
(0xaa1302104d09489ea2c6b26cb13269c9, 'số nhà 15, ngõ 276 Đai Từ', b'0', '0944711632', 'Nguyen Minh Quang', 'quang3', 1490, 201, '1A0801'),
(0xaaf0d39fd48544a5848ed739c59efcf8, 'số nhà 20', b'0', '0336251234', 'Nguyen Van Hoa', 'quang3', 1965, 248, '180521'),
(0xbb2f028fa3234de2b8ac9bff2d4a1275, '', b'0', '0336032604', 'Nguyen Thanh Dien', 'quang3', 2060, 265, '620202'),
(0xe30bcc340d064478b6bf31a2475e1fbd, 'Xóm 4 Ninh Mỹ', b'1', '0944711632', 'Nguyễn Thị Linh', 'quang3', 1840, 231, '251012'),
(0xe4c34c625ab548b3a38fe2c651daac4d, 'so nha 20', b'0', '0863421123', 'Nguyen Thi Hoa', 'quang1', 2170, 265, '620910'),
(0xe718495563b5452eb3bd3b1a590854c4, 'Xóm 4 Ninh Mỹ', b'1', '0338032604', 'Nguyễn Thị Linh', 'linhxinhgai', 1840, 231, '251012'),
(0xefb5c5b906f54cb6ac60242f1508577b, '', b'0', '0944711632', 'Nguyễn Minh Quang', 'quang3', 1840, 231, '251007'),
(0xf1d838356643402c801c4a757502e799, 'Xóm 4 Ninh Mỹ', b'1', '0944711632', 'Nguyễn Minh Quang', 'quangminh', 1840, 231, '251012');

-- --------------------------------------------------------

--
-- Table structure for table `user_role`
--

CREATE TABLE `user_role` (
  `user_id` binary(16) NOT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(0x14983eea2fd74edd8ddf146c7927ed23, 1),
(0x15e425bbeae3476e970b4afc0a1f5932, 2),
(0x18dd608303824f3fb5b8fde4dec994b9, 2),
(0x20fdbd3310754cc2af1ad8213978fb7f, 2),
(0x21ae05238118489a86cf7edf55d31901, 2),
(0x26ce5564f32a4125b8cad3c89f80466c, 2),
(0x31be4722e311440eb571c33a65c3992a, 2),
(0x4e916c2f24624a0eb1322ad0699a26c3, 2),
(0x78e6465a87da45bcb30c0072453d8ce1, 2),
(0x7aea20848d0841ddb3575d9f242c3b9f, 2),
(0x8a1a64cdcaa442b98c3d37195bba136f, 2),
(0xa180561dbf4349e080760c5c466136c0, 2),
(0xab87e54994dd4c04883db71141eeedbc, 2),
(0xacd565dd760a453b844857cc1602d0ae, 2),
(0xb231fe240f1a47dcacaaf2084002431e, 2),
(0xb653efc8f9fe4ca880bcbc63ae82418b, 2),
(0xb83d3d4a9e6f45ee9140ffa8dea3d8bd, 2),
(0xcc41b62078ec4f8cb128bc21d2b8fc36, 2),
(0xd17598bdd6d0482b867c38b82d35e56c, 2),
(0xf10bf57d57384d0f94bf3e0b91bd99d1, 2),
(0xf207b218a13b4bc8aff035b41a5f2f33, 2);

-- --------------------------------------------------------

--
-- Table structure for table `wards`
--

CREATE TABLE `wards` (
  `id` varchar(255) NOT NULL,
  `district_id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wards`
--

INSERT INTO `wards` (`id`, `district_id`, `name`) VALUES
('120321', 1924, 'Xã Tân Thịnh'),
('13004', 3440, 'Phường Mỹ Đình 1'),
('140315', 3230, 'Xã Pi Toong'),
('180521', 1965, 'Xã Tiên Hưng'),
('1A0801', 1490, 'Phường Đại Kim'),
('1A1111', 1710, 'Xã Tân Triều'),
('20102', 1442, 'Phường Bến Thành'),
('221001', 2045, 'Thị trấn Văn Giang'),
('221011', 2045, 'Xã Xuân Quan'),
('230521', 2156, 'Xã Thượng Cốc'),
('230915', 2157, 'Xã Yên Bồng'),
('231012', 2270, 'Xã Yên Lạc'),
('251007', 1840, 'Hải Châu'),
('251012', 1840, 'Xã Hải Giang'),
('360508', 2205, 'Xã Sa Sơn'),
('470416', 1777, 'Xã Thuận Hòa'),
('600704', 1723, 'Xã Vĩnh Hậu'),
('620202', 2060, 'Phường Sông Đà'),
('620910', 2170, 'Xã Xuân Lao'),
('70314', 1989, 'Xã Pa Vây Sử'),
('800080', 3311, 'Xã An Sơn'),
('80213', 2264, 'Xã Thào Chư Phìn');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `author`
--
ALTER TABLE `author`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKklnrv3weler2ftkweewlky958` (`author_id`),
  ADD KEY `FKam9riv8y6rjwkua1gapdfew4j` (`category_id`);

--
-- Indexes for table `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK64t7ox312pqal3p7fg9o503c2` (`user_id`);

--
-- Indexes for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKd5p1jgglnj3gl89odc95hurot` (`book_id`),
  ADD KEY `FKpcttvuq4mxppo8sxggjtn5i2c` (`cart_id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `delivery_type`
--
ALTER TABLE `delivery_type`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `districts`
--
ALTER TABLE `districts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `index_sent_at` (`sent_at`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKka53vduoyuwwyyolhwdggjo8c` (`receiver_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKag8ppnkjvx255gj7lm3m18wkj` (`payment_id`),
  ADD KEY `FKel9kyl84ego2otj2accfd8mr7` (`user_id`),
  ADD KEY `FK4trdox2wiutge5kyr6ijig7r` (`address_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKqscqcme08spiyt2guyqdj72eh` (`book_id`),
  ADD KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `provinces`
--
ALTER TABLE `provinces`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK70yrt09r4r54tcgkrwbeqenbs` (`book_id`),
  ADD KEY `FKiyf57dy48lyiftdrf7y87rnxi` (`user_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK716hgxp60ym1lifrdgp67xt5k` (`role_name`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKlbpbnd7rjgjm1s5p3xpyil0s3` (`chatroom_id`),
  ADD UNIQUE KEY `UK74pv0ulkqqrth69630norp5h` (`room_id`);

--
-- Indexes for table `token_invalid`
--
ALTER TABLE `token_invalid`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users_liked_books`
--
ALTER TABLE `users_liked_books`
  ADD PRIMARY KEY (`user_id`,`book_id`),
  ADD KEY `FKaroyldps4n24gjg503kcx3qrx` (`book_id`);

--
-- Indexes for table `user_address`
--
ALTER TABLE `user_address`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKibncgdvm3klfjqbdfb3kpypl` (`district_id`),
  ADD KEY `FKat4rfa16kqjmos9ekfswtvpmb` (`province_id`),
  ADD KEY `FKrqr7o5wetvsyk4qbprim0p1ki` (`ward_id`);

--
-- Indexes for table `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FKt7e7djp752sqn6w22i6ocqy6q` (`role_id`);

--
-- Indexes for table `wards`
--
ALTER TABLE `wards`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `delivery_type`
--
ALTER TABLE `delivery_type`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `message`
--
ALTER TABLE `message`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=143;

--
-- AUTO_INCREMENT for table `payment`
--
ALTER TABLE `payment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=122;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `room`
--
ALTER TABLE `room`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `FKam9riv8y6rjwkua1gapdfew4j` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  ADD CONSTRAINT `FKklnrv3weler2ftkweewlky958` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`);

--
-- Constraints for table `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `FKpay9408fi1tlnkqv3fhetr6hy` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD CONSTRAINT `FKd5p1jgglnj3gl89odc95hurot` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  ADD CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`);

--
-- Constraints for table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `FKka53vduoyuwwyyolhwdggjo8c` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `FK4trdox2wiutge5kyr6ijig7r` FOREIGN KEY (`address_id`) REFERENCES `user_address` (`id`),
  ADD CONSTRAINT `FKag8ppnkjvx255gj7lm3m18wkj` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`),
  ADD CONSTRAINT `FKel9kyl84ego2otj2accfd8mr7` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `FKqscqcme08spiyt2guyqdj72eh` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`);

--
-- Constraints for table `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `FK70yrt09r4r54tcgkrwbeqenbs` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  ADD CONSTRAINT `FKiyf57dy48lyiftdrf7y87rnxi` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `users_liked_books`
--
ALTER TABLE `users_liked_books`
  ADD CONSTRAINT `FK7qe94kas6rfxa0l0crpa5atgk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKaroyldps4n24gjg503kcx3qrx` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`);

--
-- Constraints for table `user_address`
--
ALTER TABLE `user_address`
  ADD CONSTRAINT `FKat4rfa16kqjmos9ekfswtvpmb` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`),
  ADD CONSTRAINT `FKibncgdvm3klfjqbdfb3kpypl` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`),
  ADD CONSTRAINT `FKrqr7o5wetvsyk4qbprim0p1ki` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`);

--
-- Constraints for table `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKt7e7djp752sqn6w22i6ocqy6q` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
