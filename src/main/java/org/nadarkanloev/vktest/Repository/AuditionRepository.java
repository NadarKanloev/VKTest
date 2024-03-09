package org.nadarkanloev.vktest.Repository;

import org.nadarkanloev.vktest.Model.Audition;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditionRepository extends CassandraRepository<Audition, Long > {

}
