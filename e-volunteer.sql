-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 04, 2025 at 11:28 AM
-- Server version: 9.1.0
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `e-volunteer`
--

DELIMITER $$
--
-- Procedures
--
DROP PROCEDURE IF EXISTS `SignupVolunteer`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `SignupVolunteer` (IN `p_VolFname` VARCHAR(30), IN `p_VolLname` VARCHAR(30), IN `p_BirthDate` DATE, IN `p_VolContact` VARCHAR(10), OUT `p_VolunteerID` INT)   BEGIN
    SELECT MAX(VolunteerID) + 1 INTO p_VolunteerID FROM volunteer;

    IF p_VolunteerID IS NULL THEN
        SET p_VolunteerID = 1;
    END IF;

    -- Check if VolContact is empty or NULL
    IF p_VolContact IS NULL OR TRIM(p_VolContact) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'VolContact cannot be empty';
    END IF;

    INSERT INTO volunteer (VolunteerID, VolFname, VolLname, `Birth Date`, VolContact)
    VALUES (p_VolunteerID, p_VolFname, p_VolLname, p_BirthDate, p_VolContact);
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE IF NOT EXISTS `admin` (
  `AdminID` int NOT NULL,
  `AdminFname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `AdminLname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `Email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `Password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`AdminID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`AdminID`, `AdminFname`, `AdminLname`, `Email`, `Password`) VALUES
(236001, 'Juan', 'Cruz', 'Juan.Cruz@gmail.com', '1'),
(236002, 'Maria', 'Reyes', 'Maria.Reyes@gmail.com', 'MariaGanda9'),
(236003, 'Tomas', 'Santos', 'Tomas.Santos@gmail.com', 'MangTomas21'),
(236004, 'Ana', 'Garcia', 'Ana.Garcia@gmail.com', 'AnaPretty40'),
(236005, 'Pedro', 'Lopez', 'Pedro.Lopez@gmail.com', 'PedritoBatak7'),
(236006, 'Liza', 'Dela Cruz', 'Liza.Dela.Cruz@gmail.com', 'LizaSoberano5'),
(236007, 'Carlo', 'Mendoza', 'Carlo.Mendoza@gmail.com', 'CarlosYulo5'),
(236008, 'Sofia', 'Rivera', 'Sofia.Rivera@gmail.com', 'SofiaLove9'),
(236009, 'Miguel', 'Aquino', 'Miguel.Aquino@gmail.com', 'Miguelito5');

-- --------------------------------------------------------

--
-- Table structure for table `beneficiary`
--

DROP TABLE IF EXISTS `beneficiary`;
CREATE TABLE IF NOT EXISTS `beneficiary` (
  `BeneficiaryID` int NOT NULL AUTO_INCREMENT,
  `BenFname` varchar(30) DEFAULT NULL,
  `BenLname` varchar(30) DEFAULT NULL,
  `BenAddress` varchar(66) DEFAULT NULL,
  `BenContact` varchar(10) DEFAULT NULL,
  `BenType` varchar(10) DEFAULT NULL,
  `ServiceID` int DEFAULT NULL,
  PRIMARY KEY (`BeneficiaryID`),
  KEY `beneficiary_ibfk_1` (`ServiceID`)
) ENGINE=InnoDB AUTO_INCREMENT=123041 DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `beneficiary`
--

INSERT INTO `beneficiary` (`BeneficiaryID`, `BenFname`, `BenLname`, `BenAddress`, `BenContact`, `BenType`, `ServiceID`) VALUES
(123001, 'Nelson', 'Garcia', 'Session Road, Baguio City, Benguet', '2147483647', 'Community', 1),
(123002, 'Cacilie', 'Reyes', 'Burnham Park, Jose Abad Santos Drive, Baguio City', '2147483647', 'Barangay', 2),
(123003, 'Roel', 'Ramos', 'Abanao Square, Abanao Street, Baguio City', '2147483647', 'Barangay', 3),
(123004, 'Phoenix', 'Mendoza', '45 Asin Road, Baguio City, Benguet', '2147483647', 'Individual', 4),
(123005, 'Katharyn', 'Santos', 'Outlook Drive, near The Mansion, Baguio City', '2147483647', 'Community', 1),
(123006, 'Helaina', 'Flores', 'Kisad Road, near Burnham Park, Baguio City', '2147483647', 'Barangay', 3),
(123007, 'Nelson', 'Garcia', 'Session Road, Baguio City, Benguet', '2147483647', 'Community', 2),
(123008, 'Cacilia', 'Bautista', 'Loakan Road, near Philippine Military Academy, Baguio City', '2147483647', 'Individual', 4),
(123009, 'Rodrigo', 'Villanueva', 'Baguio Country Club, Country Club Road, Baguio City', '2147483647', 'Community', 1),
(123010, 'Bunnie', 'Cruz', '10 P. Burgos Street, Baguio City, Benguet', '2147483647', 'Barangay', 1),
(123011, 'Nenad', 'Lopez', 'Lake Drive, Burnham Park, Baguio City', '2147483647', 'Barangay', 2),
(123012, 'Amalle', 'Perez', 'Fr. F. Carlu Street, near Baguio Cathedral, Baguio City', '2147483647', 'Community', 3),
(123013, 'Brynna', 'Francisco', 'F. Calderon Street, Dominican Hill, Baguio City', '2147483647', 'Individual', 4),
(123014, 'Nerissa', 'Aquino', 'U.P. Drive, near University of the Philippines Baguio, Baguio City', '2147483647', 'Community', 2),
(123015, 'Katharine', 'Rivera', 'Acacia Alley, Bakakeng, Baguio City', '2147483647', 'Barangay', 1),
(123016, 'Katha', 'Castro', 'Alos Road, Barangay Irisan, Baguio City', '2147483647', 'Barangay', 3),
(123017, 'Bunny', 'Diaz', 'Gardenia Street, near Camp 7, Baguio City', '2147483647', 'Barangay', 1),
(123018, 'Bunni', 'Torres', '12 Pucusan Road, Baguio City', '2147483647', 'Individual', 4),
(123019, 'Heidi', 'Domingo', 'Camia Street, Quezon Hill, Baguio City', '2147483647', 'Community', 2),
(123020, 'Jeniffer', 'Santiago', 'Crystal Cave Road, Barangay Crystal Cave, Baguio City', '2147483647', 'Barangay', 3),
(123021, 'Roe', 'Soriano', 'Elizabeth Court, Barangay Pacdal, Baguio City', '2147483647', 'Community', 1),
(123022, 'Bunny', 'Diaz', 'Gardenia Street, near Camp 7, Baguio City', '2147483647', 'Barangay', 2),
(123023, 'Phoebe', 'Hernandez', 'Hemlock Drive, near Leonard Wood Road, Baguio City', '2147483647', 'Individual', 4),
(123024, 'Helaine', 'Tolentino', 'Ilang-ilang Street, Barangay Aurora Hill, Baguio City', '2147483647', 'Community', 1),
(123025, 'Brynne', 'Valdez', 'J. Smith Drive, near Teachers Camp, Baguio City', '2147483647', 'Barangay', 3),
(123026, 'Jeniece', 'Ramirez', 'Lamug Street, Guisad, Baguio City', '2147483647', 'Individual', 4),
(123027, 'Amanda', 'Morales', 'Molave Drive, near Botanical Garden, Baguio City', '2147483647', 'Community', 2),
(123028, 'Rodrigus', 'Mercado', 'Narra Street, Barangay Brookside, Baguio City', '2147483647', 'Barangay', 1),
(123029, 'Katharina', 'Aguilar', 'Pine Tree Alley, near Wright Park, Baguio City', '2147483647', 'Community', 3),
(123030, 'Kathe', 'Navarro', 'Quezon Hill Road 4, Quezon Hill, Baguio City', '2147483647', 'Barangay', 2),
(123031, 'Cahra', 'Manalo', 'Remedios Hill, Camp 8, Baguio City', '2147483647', 'Community', 1),
(123032, 'Katerina', 'Gomez', 'Saint Joseph Street, near Lourdes Grotto, Baguio City', '2147483647', 'Individual', 4),
(123033, 'Jenica', 'Dizon', 'Sampaguita Street, Barangay Camp 7, Baguio City', '2147483647', 'Barangay', 3),
(123034, 'Jenilee', 'Javier', 'San Luis Extension Road, Barangay San Luis, Baguio City', '2147483647', 'Community', 2),
(123035, 'Buffy', 'Salvador', 'Smith Road, Barangay Bakakeng, Baguio City', '2147483647', 'Community', 1),
(123036, 'Heidie', 'Mariano', 'Yellow Pine Street, Barangay Gibraltar, Baguio City', '2147483647', 'Community', 4),
(123037, 'Katey', 'Agustin', 'Tamarack Street, Barangay Pacdal, Baguio City', '2147483647', 'Barangay', 3),
(123038, 'Amalie', 'Pineda', 'Vicente Puyat Avenue, near Teachers Camp, Baguio City', '2147483647', 'Community', 2),
(123039, 'Katerine', 'Ocampo', 'White Pine Street, near Baguio Botanical Garden', '2147483647', 'Barangay', 1),
(123040, 'Heidie', 'Mariano', 'Yellow Pine Street, Barangay Gibraltar, Baguio City', '2147483647', 'Community', 3);

-- --------------------------------------------------------

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
CREATE TABLE IF NOT EXISTS `resource` (
  `ResourceId` int NOT NULL AUTO_INCREMENT,
  `ResourceName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ResourceQuantity` int DEFAULT NULL,
  `ResourceFunds` decimal(12,2) DEFAULT NULL,
  `ResourceDateAllocated` date DEFAULT NULL,
  `ServiceID` int DEFAULT NULL,
  PRIMARY KEY (`ResourceId`),
  KEY `ServiceID` (`ServiceID`)
) ENGINE=InnoDB AUTO_INCREMENT=1051 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resource`
--

INSERT INTO `resource` (`ResourceId`, `ResourceName`, `ResourceQuantity`, `ResourceFunds`, `ResourceDateAllocated`, `ServiceID`) VALUES
(1001, 'Trash Bags', 200, NULL, '2025-03-12', 1),
(1002, 'Gloves (Pairs)', 300, NULL, '2025-03-12', 1),
(1003, 'Shovels', 100, NULL, '2025-03-13', 2),
(1004, 'Tree Saplings', 1000, NULL, '2025-03-14', 2),
(1005, 'Watering Cans', 200, NULL, '2025-03-15', 2),
(1006, 'Compost Bins', 150, NULL, '2025-03-16', 3),
(1007, 'Segregation Bins (Sets)', 250, NULL, '2025-03-17', 3),
(1008, 'Waste Segregation Funds (PHP)', NULL, 20000.00, '2025-03-18', 3),
(1009, 'First Aid Kits (Sets)', 100, NULL, '2025-03-19', 4),
(1010, 'Face Masks', 5000, NULL, '2025-03-20', 4),
(1011, 'Cleaning Solution', 200, NULL, '2025-03-21', 4),
(1012, 'Rakes', 100, NULL, '2025-03-22', 2),
(1013, 'Hand Sanitizers', 1000, NULL, '2025-03-23', 4),
(1014, 'Bandages (Packs)', 500, NULL, '2025-03-24', 4),
(1015, 'Blood Pressure Monitors', 50, NULL, '2025-03-25', 4),
(1016, 'Stethoscopes', 60, NULL, '2025-03-26', 4),
(1017, 'Cash Donation (PHP)', NULL, 15000.00, '2025-03-27', 2),
(1018, 'Garbage Collection Trucks', 1, NULL, '2025-03-28', 1),
(1019, 'Community Clean-up Funds (PHP)', NULL, 50000.00, '2025-03-29', 1),
(1020, 'Medical Assistance Funds (PHP)', NULL, 100000.00, '2025-03-30', 4),
(1021, 'Eco-Friendly Trash Bags', 1000, NULL, '2025-03-31', 3),
(1022, 'Wheelchairs', 30, NULL, '2025-04-01', 4),
(1023, 'Nebulizers', 40, NULL, '2025-04-02', 4),
(1024, 'Vitamin Packs (Sets)', 2000, NULL, '2025-04-03', 4),
(1025, 'Pain Relievers', 550, NULL, '2025-04-04', 4),
(1026, 'Bottled Water', 5000, NULL, '2025-04-05', 1),
(1027, 'Face Shields', 400, NULL, '2025-04-06', 4),
(1028, 'Raincoats for Volunteers', 500, NULL, '2025-04-07', 1),
(1029, 'Medical Assistance Funds (PHP)', NULL, 56000.00, '2025-04-08', 4),
(1030, 'Medical Gloves (Pairs)', 5000, NULL, '2025-04-09', 4),
(1031, 'Cleaning Brooms', 300, NULL, '2025-04-10', 1),
(1032, 'Tree-Planting Event Funds (PHP)', NULL, 45000.00, '2025-04-11', 2),
(1033, 'Dust Masks', 2000, NULL, '2025-04-12', 3),
(1034, 'Environmental Awareness Flyers', 10000, NULL, '2025-04-13', 1),
(1035, 'Bamboo Poles', 100, NULL, '2025-04-14', 2),
(1036, 'Community Garden Soil (Tons)', 10, NULL, '2025-04-15', 2),
(1037, 'Recycled Paper Bags', 1000, NULL, '2025-04-16', 3),
(1038, 'Biodegradable Straws', 5000, NULL, '2025-04-17', 3),
(1039, 'Blood Donation Drive Support (PHP)', NULL, 75000.00, '2025-04-18', 4),
(1040, 'Emergency Blankets', 200, NULL, '2025-04-19', 4),
(1041, 'Tree-Planting Event Funds (PHP)', NULL, 80000.00, '2025-04-20', 2),
(1042, 'Herbal Medicines (Packs)', 1500, NULL, '2025-04-21', 2),
(1043, 'Seedlings', 3000, NULL, '2025-04-22', 2),
(1044, 'Bamboo Seedlings', 1500, NULL, '2025-04-23', 2),
(1045, 'Portable Trash Pickers', 250, NULL, '2025-04-24', 3),
(1046, 'Medical Check-up Booths (Tents)', 10, NULL, '2025-04-25', 4),
(1047, 'Trash Sorting Labels', 170, NULL, '2025-04-26', 3),
(1048, 'LED Flashlights', 50, NULL, '2025-04-27', 2),
(1049, 'Medical Transport Vehicles (Ambulance)', 2, NULL, '2025-04-28', 4),
(1050, 'Cash Donation (PHP)', NULL, 55000.00, '2025-04-29', 4);

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
CREATE TABLE IF NOT EXISTS `service` (
  `ServiceID` int NOT NULL AUTO_INCREMENT,
  `ServiceDetails` varchar(18) DEFAULT NULL,
  `MaxNumVolunteers` int DEFAULT NULL,
  PRIMARY KEY (`ServiceID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `service`
--

INSERT INTO `service` (`ServiceID`, `ServiceDetails`, `MaxNumVolunteers`) VALUES
(1, 'Cleanup Drives', 50),
(2, 'Tree-Planting', 60),
(3, 'Waste Segregation', 40),
(4, 'Medical Assistance', 30),
(9, '232', 323),
(10, '23', 2);

-- --------------------------------------------------------

--
-- Table structure for table `servicedetails`
--

DROP TABLE IF EXISTS `servicedetails`;
CREATE TABLE IF NOT EXISTS `servicedetails` (
  `ServSchedID` int NOT NULL,
  `VolunteerID` int DEFAULT NULL,
  `Status` varchar(9) DEFAULT NULL,
  KEY `ServSchedID` (`ServSchedID`),
  KEY `VolunteerID` (`VolunteerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `servicedetails`
--

INSERT INTO `servicedetails` (`ServSchedID`, `VolunteerID`, `Status`) VALUES
(2010, 225001, 'Pending'),
(2011, 225001, 'Pending');

-- --------------------------------------------------------

--
-- Table structure for table `serviceschedule`
--

DROP TABLE IF EXISTS `serviceschedule`;
CREATE TABLE IF NOT EXISTS `serviceschedule` (
  `ServSchedID` int NOT NULL AUTO_INCREMENT,
  `ServiceID` int DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `Time Start` time DEFAULT NULL,
  `Time End` time DEFAULT NULL,
  `Venue` varchar(50) DEFAULT NULL,
  `Slots` int DEFAULT NULL,
  PRIMARY KEY (`ServSchedID`)
) ENGINE=InnoDB AUTO_INCREMENT=2041 DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `serviceschedule`
--

INSERT INTO `serviceschedule` (`ServSchedID`, `ServiceID`, `Date`, `Time Start`, `Time End`, `Venue`, `Slots`) VALUES
(2001, 1, '2025-07-01', '07:30:00', '09:30:00', 'Burnham Park, Baguio City', 2),
(2002, 1, '2025-07-01', '07:30:00', '09:30:00', 'Wright Park, Baguio City', 2),
(2003, 1, '2025-07-01', '08:30:00', '10:30:00', 'Camp John Hay, Baguio City', 1),
(2004, 1, '2025-07-01', '10:30:00', '12:30:00', 'Camp John Hay, Baguio City', 0),
(2005, 1, '2025-07-03', '07:00:00', '09:00:00', 'Trancoville, Baguio City', 1),
(2006, 1, '2025-07-03', '09:00:00', '10:00:00', 'Bonifacio Street, Baguio City', 2),
(2007, 1, '2025-07-05', '08:30:00', '10:30:00', 'Barangay Gibraltar', 0),
(2008, 1, '2025-07-05', '09:00:00', '11:00:00', 'Barangay Dominican Hill', 2),
(2009, 1, '2025-07-05', '10:00:00', '12:00:00', 'Barangay San Luis', 0),
(2010, 2, '2025-07-02', '09:00:00', '11:00:00', 'Melvin Jones, Baguio City', 1),
(2011, 2, '2025-07-02', '09:00:00', '11:00:00', 'Dominican Hill, Baguio City', 2),
(2012, 2, '2025-07-02', '11:00:00', '12:00:00', 'Dominican Hill, Baguio City', 1),
(2013, 2, '2025-07-10', '10:30:00', '12:30:00', 'Barangay Loakan Proper', 2),
(2014, 2, '2025-07-10', '10:30:00', '12:30:00', 'Barangay Pinsao Proper', 2),
(2015, 2, '2025-07-10', '12:00:00', '14:00:00', 'Barangay Upper QM', 3),
(2016, 3, '2025-07-08', '10:30:00', '12:30:00', 'Barangay Engineers Hill', 0),
(2017, 3, '2025-07-08', '11:00:00', '13:00:00', 'Barangay Pacdal', 0),
(2018, 3, '2025-07-08', '12:00:00', '14:00:00', 'Barangay Aurora Hill', 0),
(2019, 3, '2025-07-15', '07:30:00', '09:30:00', 'Barangay Sto. Tomas Proper', 2),
(2020, 3, '2025-07-15', '07:30:00', '09:30:00', 'Barangay Atok Trail', 0),
(2021, 3, '2025-07-15', '09:00:00', '11:00:00', 'Barangay Happy Hollow', 0),
(2022, 4, '2025-07-04', '07:30:00', '09:30:00', 'Barangay Camp 7', 1),
(2023, 4, '2025-07-04', '07:30:00', '09:30:00', 'Barangay Happy Hollow', 3),
(2024, 4, '2025-07-04', '09:00:00', '11:00:00', 'Botanical Garden, Baguio City', 1),
(2025, 4, '2025-07-18', '10:00:00', '12:00:00', 'Botanical Garden, Baguio City', 2),
(2026, 4, '2025-07-18', '14:00:00', '16:00:00', 'Barangay Loakan Proper', 1),
(2027, 4, '2025-07-18', '14:00:00', '16:00:00', 'Mines View Park, Baguio City', 1),
(2028, 1, '2025-07-08', '08:00:00', '10:00:00', 'Barangay Trancoville', 0),
(2029, 1, '2025-07-08', '08:00:00', '10:00:00', 'Barangay Loakan Proper', 0),
(2030, 2, '2025-07-09', '09:30:00', '11:30:00', 'Barangay Happy Hollow', 0),
(2031, 2, '2025-07-09', '09:30:00', '11:30:00', 'Barangay Magsaysay', 0),
(2032, 3, '2025-07-17', '10:00:00', '12:00:00', 'Barangay Sto. Niño', 2),
(2033, 3, '2025-07-17', '10:00:00', '12:00:00', 'Barangay Engineers Hill', 2),
(2034, 4, '2025-07-19', '11:00:00', '13:00:00', 'Camp John Hay, Baguio City', 0),
(2035, 4, '2025-07-19', '11:00:00', '13:00:00', 'Baguio General Hospital', 2),
(2036, 4, '2025-07-19', '08:00:00', '10:00:00', 'Barangay Magsaysay', 1),
(2037, 4, '2025-07-25', '09:00:00', '11:00:00', 'Barangay Bakakeng Norte', 0),
(2038, 4, '2025-07-25', '13:00:00', '15:00:00', 'Barangay Bakakeng Norte', 0),
(2039, 4, '2025-07-26', '10:00:00', '12:00:00', 'Wright Park, Baguio City', 0),
(2040, 4, '2025-07-31', '10:30:00', '12:30:00', 'Wright Park, Baguio City', 1);

-- --------------------------------------------------------

--
-- Table structure for table `volunteer`
--

DROP TABLE IF EXISTS `volunteer`;
CREATE TABLE IF NOT EXISTS `volunteer` (
  `VolunteerID` int NOT NULL AUTO_INCREMENT,
  `VolFname` varchar(30) DEFAULT NULL,
  `VolLname` varchar(30) DEFAULT NULL,
  `Birth Date` date DEFAULT NULL,
  `VolContact` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`VolunteerID`),
  UNIQUE KEY `VolContact` (`VolContact`)
) ENGINE=InnoDB AUTO_INCREMENT=225045 DEFAULT CHARSET=utf8mb3;

--
-- Dumping data for table `volunteer`
--

INSERT INTO `volunteer` (`VolunteerID`, `VolFname`, `VolLname`, `Birth Date`, `VolContact`) VALUES
(225001, 'Ashana', 'Aquino', '2006-06-15', '9123456789'),
(225002, 'Zee', 'Bautista', '1990-06-15', '9134567890'),
(225003, 'Jkent', 'Cruz', '1997-06-15', '9145678901'),
(225004, 'Jasper', 'Dela Cruz', '1999-06-15', '9156789012'),
(225005, 'Julian', 'Garcia', '1995-06-15', '9167890123'),
(225006, 'Jedrick', 'Herrera', '2003-06-15', '9178901234'),
(225007, 'Richard', 'Jimenez', '1983-06-15', '9189012345'),
(225008, 'Jimrey', 'Lopez', '1998-06-15', '9190123456'),
(225009, 'Raemond', 'Mendoza', '1996-06-15', '9101234567'),
(225010, 'Stephen', 'Navarro', '1994-06-15', '9112345678'),
(225011, 'Juhan', 'Ortega', '2007-06-15', '9123456780'),
(225012, 'Daniel', 'Perez', '1980-06-15', '9134567891'),
(225013, 'Ken', 'Reyes', '1997-06-15', '9145678902'),
(225014, 'Jiro', 'Santos', '1999-06-15', '9156789013'),
(225015, 'Klouie', 'Torres', '1993-06-15', '9167890124'),
(225016, 'Rainer', 'Valdez', '1995-06-15', '9178901235'),
(225017, 'Dennis', 'Zamora', '1987-06-15', '9189012346'),
(225018, 'Andres', 'Alvarado', '1998-06-15', '9190123457'),
(225019, 'Bea', 'Bayani', '2000-06-15', '9101234568'),
(225020, 'Carlos', 'Carpio', '1985-06-15', '9112345679'),
(225021, 'Daisy', 'Del Rosario', '1988-06-15', '9123456781'),
(225022, 'Elias', 'Gamboa', '1986-06-15', '9134567892'),
(225023, 'Faye', 'Ponce', '1996-06-15', '9145678903'),
(225024, 'Gabriel', 'Rojas', '1991-06-15', '9156789014'),
(225025, 'Hana', 'Sison', '1992-06-15', '9167890125'),
(225026, 'Iñigo', 'Tanco', '1984-06-15', '9178901236'),
(225027, 'Jaime', 'Villanueva', '1997-06-15', '9189012347'),
(225028, 'Kiana', 'Yap', '2000-06-15', '9190123458'),
(225029, 'Luis', 'Encarnacion', '1990-06-15', '9101234569'),
(225030, 'Maria', 'Fajardo', '1975-06-15', '9112345680'),
(225031, 'Nicanor', 'Galvez', '1996-06-15', '9123456782'),
(225032, 'Olivia', 'Macapagal', '1989-06-15', '9134567893'),
(225033, 'Paolo', 'Manlapig', '1977-06-15', '9145678904'),
(225034, 'Quinlyn', 'Mercado', '1973-06-15', '9156789015'),
(225035, 'Renato', 'Pascual', '1998-06-15', '9167890126'),
(225036, 'Sara', 'Quinto', '1985-06-15', '9178901237'),
(225037, 'Tomas', 'San Juan', '1972-06-15', '9189012348'),
(225038, 'Uriel', 'Tagle', '1981-06-15', '9190123459'),
(225039, 'Vicky', 'Varela', '1984-06-15', '9101234570'),
(225040, 'Wendel', 'Zubiri', '1976-06-15', '9112345681'),
(225041, 'Jiro', 'Reyes', '2004-09-25', '9111111111');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `beneficiary`
--
ALTER TABLE `beneficiary`
  ADD CONSTRAINT `beneficiary_ibfk_1` FOREIGN KEY (`ServiceID`) REFERENCES `service` (`ServiceID`) ON DELETE CASCADE;

--
-- Constraints for table `resource`
--
ALTER TABLE `resource`
  ADD CONSTRAINT `resource_ibfk_1` FOREIGN KEY (`ServiceID`) REFERENCES `service` (`ServiceID`) ON UPDATE CASCADE;

--
-- Constraints for table `servicedetails`
--
ALTER TABLE `servicedetails`
  ADD CONSTRAINT `servicedetails_ibfk_1` FOREIGN KEY (`ServSchedID`) REFERENCES `serviceschedule` (`ServSchedID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `servicedetails_ibfk_2` FOREIGN KEY (`VolunteerID`) REFERENCES `volunteer` (`VolunteerID`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
