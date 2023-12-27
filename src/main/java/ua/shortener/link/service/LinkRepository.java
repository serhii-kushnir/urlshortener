package ua.shortener.link.service;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.shortener.link.Link;

public interface LinkRepository extends JpaRepository<Link, String> {
}
