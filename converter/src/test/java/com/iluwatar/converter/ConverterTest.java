package com.iluwatar.converter;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;

public class ConverterTest {

  private UserConverter userConverter = new UserConverter();

  /**
   * Tests whether a converter created of opposite functions holds equality as a bijection.
   */
  @Test public void testConversionsStartingFromDomain() {
    User u1 = new User("Tom", "Hanks", true, "tom@hanks.com");
    User u2 = userConverter.convertFromDto(userConverter.convertFromEntity(u1));
    assertEquals(u1, u2);
  }

  /**
   * Tests whether a converter created of opposite functions holds equality as a bijection.
   */
  @Test public void testConversionsStartingFromDto() {
    UserDto u1 = new UserDto("Tom", "Hanks", true, "tom@hanks.com");
    UserDto u2 = userConverter.convertFromEntity(userConverter.convertFromDto(u1));
    assertEquals(u1, u2);
  }

  /**
   * Tests the custom users converter. Thanks to Java8 lambdas, converter can be easily and
   * cleanly instantiated allowing various different conversion strategies to be implemented.
   */
  @Test public void testCustomConverter() {
    Converter<UserDto, User> converter = new Converter<>(
        userDto -> new User(userDto.getFirstName(), userDto.getLastName(), userDto.isActive(),
        String.valueOf(new Random().nextInt())),
        user -> new UserDto(user.getFirstName(), user.getLastName(), user.isActive(),
        user.getFirstName().toLowerCase() + user.getLastName().toLowerCase() + "@whatever.com"));
    User u1 = new User("John", "Doe", false, "12324");
    UserDto userDto = converter.convertFromEntity(u1);
    assertEquals(userDto.getEmail(), "johndoe@whatever.com");
  }

  /**
   * Test whether converting a collection of Users to DTO Users and then converting them back to domain
   * users returns an equal collection.
   */
  @Test public void testCollectionConversion() {
    ArrayList<User> users = Lists.newArrayList(new User("Camile", "Tough", false, "124sad"),
        new User("Marti", "Luther", true, "42309fd"), new User("Kate", "Smith", true, "if0243"));
    List<User> fromDtos = userConverter.createFromDtos(userConverter.createFromEntities(users));
    assertEquals(fromDtos, users);
  }
}
