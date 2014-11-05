-- phpMyAdmin SQL Dump
-- version 4.1.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 05, 2014 at 03:39 PM
-- Server version: 5.5.8
-- PHP Version: 5.3.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `pasaribu_store`
--

-- --------------------------------------------------------

--
-- Table structure for table `barang`
--

CREATE TABLE IF NOT EXISTS `barang` (
  `id_barang` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `id_merek` int(11) NOT NULL,
  `id_penjual` int(11) NOT NULL,
  `id_gambar` int(11) NOT NULL,
  `nama_barang` varchar(200) NOT NULL,
  `stok_barang` int(11) NOT NULL,
  `satuan_barang` varchar(50) NOT NULL,
  `harga_barang` int(11) NOT NULL,
  `tgl_harga_stok_barang` date NOT NULL,
  `kode_barang` varchar(20) NOT NULL,
  `lokasi_barang` varchar(20) NOT NULL,
  `kategori_barang` varchar(20) NOT NULL,
  `deskripsi_barang` text NOT NULL,
  `favorite` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_barang`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `barang`
--

INSERT INTO `barang` (`id_barang`, `id_user`, `id_merek`, `id_penjual`, `id_gambar`, `nama_barang`, `stok_barang`, `satuan_barang`, `harga_barang`, `tgl_harga_stok_barang`, `kode_barang`, `lokasi_barang`, `kategori_barang`, `deskripsi_barang`, `favorite`) VALUES
(1, 1, 4, 2, 2, 'Lampu Depan V.R New', 20, 'kotak', 60000, '2014-07-22', '', '', 'automotif', 'Lampu Depan V.R New dengan kualitas prima', 1);

-- --------------------------------------------------------

--
-- Table structure for table `barang_favorite`
--

CREATE TABLE IF NOT EXISTS `barang_favorite` (
  `id_favorite` int(11) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `tgl_favorite` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `belanja`
--

CREATE TABLE IF NOT EXISTS `belanja` (
  `id_belanja` int(11) NOT NULL AUTO_INCREMENT,
  `id_barang` int(11) NOT NULL,
  `tgl_belanja` date NOT NULL,
  `status_belanja` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_belanja`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `favorite`
--

CREATE TABLE IF NOT EXISTS `favorite` (
  `id_favorite` int(11) NOT NULL AUTO_INCREMENT,
  `warna_favorite` varchar(10) NOT NULL,
  `nama_favorite` varchar(50) NOT NULL,
  `deskripsi` varchar(100) NOT NULL,
  PRIMARY KEY (`id_favorite`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `gambar_barang`
--

CREATE TABLE IF NOT EXISTS `gambar_barang` (
  `id_gambar` int(11) NOT NULL AUTO_INCREMENT,
  `lokasi_gambar` varchar(200) NOT NULL,
  `deskripsi_gambar` varchar(200) NOT NULL,
  PRIMARY KEY (`id_gambar`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `gambar_barang`
--

INSERT INTO `gambar_barang` (`id_gambar`, `lokasi_gambar`, `deskripsi_gambar`) VALUES
(2, 'http://dev.mysql.com/common/logos/logo-mysql-110x57.png', 'Gambar Lampu Depan V.R New');

-- --------------------------------------------------------

--
-- Table structure for table `log_harga`
--

CREATE TABLE IF NOT EXISTS `log_harga` (
  `id_log_harga` int(11) NOT NULL AUTO_INCREMENT,
  `id_barang` int(11) NOT NULL,
  `harga` int(11) NOT NULL,
  `tgl_harga` date NOT NULL,
  `potongan_harga` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_log_harga`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `log_stok`
--

CREATE TABLE IF NOT EXISTS `log_stok` (
  `id_log_stok` int(11) NOT NULL AUTO_INCREMENT,
  `id_barang` int(11) NOT NULL,
  `jumlah_stok` int(11) NOT NULL,
  `tgl_muat_stok` date NOT NULL,
  PRIMARY KEY (`id_log_stok`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `log_transaksi`
--

CREATE TABLE IF NOT EXISTS `log_transaksi` (
  `id_transaksi` int(11) NOT NULL AUTO_INCREMENT,
  `id_barang` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `tgl_transaksi` int(11) NOT NULL,
  `keterangan` int(11) NOT NULL,
  PRIMARY KEY (`id_transaksi`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `merek`
--

CREATE TABLE IF NOT EXISTS `merek` (
  `id_merek` int(11) NOT NULL AUTO_INCREMENT,
  `nama_merek` varchar(100) NOT NULL,
  `logo_merek` varchar(200) NOT NULL,
  `deskripsi_merek` varchar(200) NOT NULL,
  PRIMARY KEY (`id_merek`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `merek`
--

INSERT INTO `merek` (`id_merek`, `nama_merek`, `logo_merek`, `deskripsi_merek`) VALUES
(1, 'Samsung', '', 'Samsung Elektronik'),
(2, 'LG', '', 'LG Electronic'),
(3, 'Polytron', '', 'Merek Taiwan'),
(4, 'Aspira', '', 'Merek Jepang, kualitas prima'),
(5, 'Ichidai', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `penjual`
--

CREATE TABLE IF NOT EXISTS `penjual` (
  `id_penjual` int(11) NOT NULL AUTO_INCREMENT,
  `nama_penjual` varchar(150) NOT NULL,
  `nama_toko` varchar(100) NOT NULL,
  `alamat_penjual` varchar(200) NOT NULL,
  `geolocation` varchar(100) NOT NULL,
  `kontak_penjual` varchar(100) NOT NULL,
  `email_penjual` varchar(100) NOT NULL,
  PRIMARY KEY (`id_penjual`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `penjual`
--

INSERT INTO `penjual` (`id_penjual`, `nama_penjual`, `nama_toko`, `alamat_penjual`, `geolocation`, `kontak_penjual`, `email_penjual`) VALUES
(1, 'Umak Riska', 'Hikmah Jaya', 'Jalan Merdeka, Padangsidimpuan', '', '', 'hikmah_jaya@gmail.com'),
(2, 'Bintang Motor Staff', 'Bintang Motor', 'Padangsidimpuan', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `email` varchar(200) NOT NULL,
  `password` varchar(50) NOT NULL,
  `pin` int(6) NOT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `user_name`, `email`, `password`, `pin`) VALUES
(1, 'anwar', 'anwar_pasaribu@live.com', 'pangarongan', 7799);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
