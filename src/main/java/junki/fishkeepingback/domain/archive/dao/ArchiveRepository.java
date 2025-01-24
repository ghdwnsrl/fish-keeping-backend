package junki.fishkeepingback.domain.archive.dao;

import junki.fishkeepingback.domain.archive.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArchiveRepository extends JpaRepository<Archive, Long>, CustomArchiveRepository {
    Optional<Archive> findByNameAndUserUsername(String name, String username);
}
