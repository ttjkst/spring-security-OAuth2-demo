package org.oauth.authoriaztion.user;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.oauth.authoriaztion.authority.AuthorityAttr;
import org.oauth.authoriaztion.authority.AuthorityEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserInfoDetailService implements UserDetailsService {


    private static List<UserInfo> userInfos;

    private String path ="userInfos.json";

    private final String premitAll="permitAll()";


    private ObjectMapper objectMapper = new ObjectMapper();


    public UserInfoDetailService() throws IOException {

        ClassPathResource fileRource = new ClassPathResource( path );
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionLikeType(List.class, UserInfoEnity.class);
        Collection<UserInfoEnity> userInfoEnities = (Collection<UserInfoEnity>) objectMapper.readValue(fileRource.getFile(), javaType);
        userInfos =  userInfoEnities.stream().map( this::mapToUserInfo).collect( Collectors.toList());

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username==null||!StringUtils.hasText( username )){
            throw new UsernameNotFoundException( "null or empty username not exist!");
        }

        Optional<UserInfo> first = userInfos.stream().filter( x -> x.getUsername().equals( username ) ).findFirst();
        return first.orElseThrow( ()->new UsernameNotFoundException( String.format( " witch name %s can not found userInfo",username)));
    }

    private UserInfo mapToUserInfo(UserInfoEnity enity){
        Set<AuthorityEntity> entities = enity.getAuthorityEntities();
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = enity.getAuthorities()
                .stream()
                .map( SimpleGrantedAuthority::new )
                .collect( Collectors.toSet());
        //due to spring default password encoder will be configured,use default password encoder --bcrypt
        String encodePassword = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(enity.getPassword());
        return new UserInfo(enity.getUsername(),encodePassword,simpleGrantedAuthorities,entities);
    }

    private LinkedList<AuthorityAttr> mapToAuthorityAttrs(Collection<AuthorityEntity> authorityEntities){
        LinkedList<AuthorityAttr> linkedList= new LinkedList<AuthorityAttr>();
        authorityEntities.forEach(entity->{
            AntPathRequestMatcher antPath = new AntPathRequestMatcher(entity.getPath(),null,
                    false,
                    null);
            AuthorityAttr attr;
            if(entity.getAny()){
                attr = new AuthorityAttr(premitAll, antPath, true);
            }else {
                attr = new AuthorityAttr(entity.getAuthority(), antPath, false);
            }
            linkedList.add(attr);
        });
        return linkedList;
    }

}
