package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.UserDTO;
import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.repositories.RoleRepository;
import com.devsuperior.bds04.repositories.UserRepository;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(c -> new UserDTO(c));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow( () -> new ResourceNotFoundException("Entidade não encontrada"));
		return new UserDTO(entity);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);
		if(user == null) {
			logger.error("Usuário não encontrado: " + username);
			throw new UsernameNotFoundException("Email não encontrado");
		}
		logger.info("Usuário encontrado: " + username);
		return user;
	}
}
