package ro.agitman.opinion.security;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.agitman.opinion.db.MongoProvider;
import ro.agitman.opinion.db.model.Role;
import ro.agitman.opinion.db.model.User;

import com.mongodb.MongoClient;

public class MongoRealm extends AuthorizingRealm {

	private static Logger LOGGER = LoggerFactory.getLogger(MongoRealm.class);

	private MongoProvider mongoProvider; // a singleton for accessing MongoDB
	private MongoClient mongoClient = null;
	private Morphia morphia = null;
	private Datastore ds = null;

	public MongoRealm() {
		LOGGER.debug("MongoRealm()");
		if (mongoClient == null) {
			try {
				mongoClient = new MongoClient("localhost");
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
			}
		}
		if (morphia == null && mongoClient != null) {
			morphia = new Morphia();
			// edit: corrected wrong package name
			morphia.mapPackage("ro.agitman.opinion.db.model");
		}
		if (ds == null && morphia != null) {
			ds = morphia.createDatastore(mongoClient, "opinion");
		}

		if (mongoClient == null && mongoProvider != null) {
			mongoClient = mongoProvider.getMongoClient();
		}
		if (ds == null && mongoProvider != null) {
			ds = mongoProvider.getDs();
		}
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		Set<String> roles = new HashSet<>();
		Set<Permission> permissions = new HashSet<>();
		for (Object tmp : pc.fromRealm(getClass().getName())) {
			User user = ds.find(User.class).field("name").equal(tmp.toString()).get();
			if (user != null) {
				roles.addAll(user.getRoles());
				for (String temp : roles) {
					Query<Role> q = ds.createQuery(Role.class);
					q.field("name").equal(temp);
					Role role = q.get();
					if (role != null) {
						permissions.addAll(role.getPermissions());
					}
				}
			}
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setRoles(roles);
		info.setObjectPermissions(permissions);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {
		User user = ds.find(User.class).field("name").equal(at.getPrincipal().toString()).get();

		if (user != null) {
			if (user.getPassword().equals(new String((char[]) at.getCredentials()))) {
				return new SimpleAuthenticationInfo(user.getName(), user.getPassword(), getClass().getName());
			}
		}
		throw new AuthenticationException();
	}

}
