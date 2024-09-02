package com.example.be.repository;

import com.example.be.Request.FoodRemainingDays;
import com.example.be.data.entity.RefrigeRator;
import com.example.be.data.entity.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User,String> {

    @Query("MATCH (u:user {id: $userId}) RETURN u")
    Optional<User> findByUserID(String userId);

    @Query("MATCH (u:user) WHERE u.id IN $userIds RETURN u")
    List<User> findByUserIds(List<String> userIds);


    @Query("MATCH (u:user {id: $userId})-[:OWNS]->(r:RefrigeRator) RETURN r.refrigerator_id AS refrigerator_id, r.refrigeratorName AS refrigeratorName")
    List<RefrigeRator> findRefrigeratorsByUserId(String userId);


    @Query("MATCH (u:user)-[r:OWNS]->(rfr:RefrigeRator {refrigerator_id: $refrigeratorId}) WHERE u.id IN $ids DELETE r")
    void deleteByIdInAndRefrigerators_RefrigeratorId(List<String> ids, String refrigeratorId);

    //유저 구독 :u가 :us를 구독
    @Query("MATCH (u:user {id: $userId1}) " +
            "MATCH (us:user {id: $userId2}) " +
            "MERGE (u)-[:userSUB]->(us)")
    void createUserSubscribe(String userId1,String userId2);
    //유저 구독 취소
    @Query("MATCH (u:user {id: $userId1})-[r:userSUB]->(us:user {id: $userid2}) DELETE r")
    void deleteUserSUBscribe(String userId1, String userId2);

    //구독자 확인    :u가 구독한 us 호출
    @Query("MATCH (u:user {id:$userId})-[:userSUB]->(us:user) RETURN us")
    List<User> findUserBySubscribe(String userId);


    //냉장고 받으면 관계형성 유저
    @Query("MATCH (r:RefrigeRator {refrigerator_id:$refrigerator_id})<-[:OWNS]-(u:user) RETURN u")
    List<User> findUserByRefrigerator(String refrigerator_id);




    @Query("MATCH (b:food) " +
            "WHERE b.expiryDate IS NOT NULL AND b.createdDate IS NOT NULL " +
            "WITH b, duration.between(date(b.createdDate), date(b.expiryDate)).days AS remainingDays " +
            "WHERE remainingDays = 1 " +
            "RETURN b.refrigeratorName AS refrigerator_id, " +
            "       remainingDays")
    List<FoodRemainingDays> findRefrigeratorIdAndRemainingDays();



    //유저정보
    @Query("MATCH (u:user {id:$userId}) return u")
    List<User> findUser(@Param("userId") String userId);






}
