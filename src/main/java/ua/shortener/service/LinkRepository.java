package ua.shortener.service;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.shortener.entity.Link;

public interface LinkRepository extends JpaRepository<Link, String> {
}
