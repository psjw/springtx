package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LogRepository {
//    @PersistenceContext //예전버전은 무조건 넣어주어야함
    private final EntityManager em;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Log logMessage){
        log.info("log 저장");
        em.persist(logMessage);

        if(logMessage.getMessage().contains("로그예외")){
            log.info("log 저장시 예외 발생");
            throw new RuntimeException("예외 발생");
        }
    }


    public Optional<Log> find(String message){
        return em.createQuery("select l from Log l where l.message = :message", Log.class)
                .setParameter("message", message)
                .getResultList().stream()
                .findAny();
    }
}

