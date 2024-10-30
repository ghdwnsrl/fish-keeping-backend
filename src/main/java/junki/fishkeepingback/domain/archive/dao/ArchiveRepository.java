package junki.fishkeepingback.domain.archive.dao;

import junki.fishkeepingback.domain.archive.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArchiveRepository extends JpaRepository<Archive, Long>, CustomArchiveRepository {
    Optional<Archive> findByName(String name);
}
