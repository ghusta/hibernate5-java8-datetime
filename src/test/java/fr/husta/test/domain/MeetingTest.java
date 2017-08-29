package fr.husta.test.domain;

import org.hibernate.cfg.AvailableSettings;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.FileInputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class MeetingTest {

    private static final Logger log = LoggerFactory.getLogger(MeetingTest.class);

    private static Properties dbProperties;

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeClass
    public static void setUpGlobal() throws Exception {
        dbProperties = new Properties();
        dbProperties.load(new FileInputStream("src/test/resources/db.properties"));
        assertThat(dbProperties).isNotEmpty();

        Map properties = new HashMap();
        properties.put(org.hibernate.cfg.AvailableSettings.JPA_JDBC_URL, dbProperties.getProperty("db.url"));
        properties.put(AvailableSettings.JPA_JDBC_USER, dbProperties.getProperty("db.username"));
        properties.put(AvailableSettings.JPA_JDBC_PASSWORD, dbProperties.getProperty("db.password"));
        entityManagerFactory = Persistence.createEntityManagerFactory("defaultPU", properties);
    }

    @Before
    public void setUp() throws Exception {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Test
    public void testDbCnx() throws Exception {
        entityManager.getTransaction().begin();

        // do nothing

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    public void testInsert() throws Exception {
        entityManager.getTransaction().begin();

        Meeting newMeeting = createMeeting();
        log.debug("#1 OffsetDateTime = {}", newMeeting.getOffsetDateTime());
        log.debug("#1 ZonedDateTime = {}", newMeeting.getZonedDateTime());
        entityManager.persist(newMeeting);

        ZoneId japanZoneId = ZoneId.of("Asia/Tokyo");
        Meeting newMeetingZoned = createMeetingWithTimeZone(japanZoneId);
        log.debug("#2 OffsetDateTime = {}", newMeetingZoned.getOffsetDateTime());
        log.debug("#2 ZonedDateTime = {}", newMeetingZoned.getZonedDateTime());
        entityManager.persist(newMeetingZoned);

        ZoneId pacificZoneId = ZoneId.of("America/Los_Angeles");
        Meeting newMeetingZoned_2 = createMeetingWithTimeZone(pacificZoneId);
        log.debug("#3 OffsetDateTime = {}", newMeetingZoned_2.getOffsetDateTime());
        log.debug("#3 OffsetDateTime = {}", newMeetingZoned_2.getZonedDateTime());
        entityManager.persist(newMeetingZoned_2);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private Meeting createMeeting() {
        Meeting newMeeting = new Meeting();
        newMeeting.setDate(LocalDate.now());
        newMeeting.setDateTime(LocalDateTime.now());
        newMeeting.setTime(LocalTime.now());

        newMeeting.setOffsetDateTime(OffsetDateTime.now());
        newMeeting.setOffsetTime(OffsetTime.now());

        newMeeting.setZonedDateTime(ZonedDateTime.now());

        newMeeting.setInstant(Instant.now());
        return newMeeting;
    }

    private Meeting createMeetingWithTimeZone(ZoneId zoneId) {
        Meeting newMeeting = new Meeting();
        newMeeting.setDate(LocalDate.now());
        newMeeting.setDateTime(LocalDateTime.now());
        newMeeting.setTime(LocalTime.now());

        newMeeting.setOffsetDateTime(OffsetDateTime.now(zoneId));
        newMeeting.setOffsetTime(OffsetTime.now(zoneId));

        newMeeting.setZonedDateTime(ZonedDateTime.now(zoneId));

        newMeeting.setInstant(Instant.now());
        return newMeeting;
    }

    @Test
    public void testFindAll() throws Exception {
        entityManager.getTransaction().begin();

        List<Meeting> meetingList;
        TypedQuery<Meeting> typedQuery = entityManager.createQuery("select m from Meeting m", Meeting.class);
        meetingList = typedQuery.getResultList();

        for (Meeting meeting : meetingList) {
            log.debug(" * {} - {}", meeting.getId(), meeting.toString());
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

}