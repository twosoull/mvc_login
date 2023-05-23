package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long,Member> store = new HashMap<>(); //static 사용
    private static Long sequence = 0L;

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save : member={}", member);
        store.put(member.getId(),member);
        return member;
    }

    public Optional<Member> findByLoginId(String loginId){
        /*
        List<Member> memberList = findAll();
        for (Member m : memberList) {
            if(m.getLoginId().equals(loginId)) {
                return m;
            }
        }
        return null;
        */
    //  위와 같은 코드
    // stream()은 for문과 같은 역할을 하며 list를 돌려준다
    // filter는 if문의 역할을 하며
    // findFirst()는 처음으로 나오는 객체를 반환해준다.

        return findAll().stream().filter(m -> m.getLoginId().equals(loginId)).findFirst();
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }

}
