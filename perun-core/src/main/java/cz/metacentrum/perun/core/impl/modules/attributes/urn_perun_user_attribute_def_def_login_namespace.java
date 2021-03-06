package cz.metacentrum.perun.core.impl.modules.attributes;

import java.util.List;

import cz.metacentrum.perun.core.api.exceptions.*;
import cz.metacentrum.perun.core.blImpl.ModulesUtilsBlImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.metacentrum.perun.core.api.Attribute;
import cz.metacentrum.perun.core.api.AttributeDefinition;
import cz.metacentrum.perun.core.api.User;
import cz.metacentrum.perun.core.impl.PerunSessionImpl;
import cz.metacentrum.perun.core.implApi.modules.attributes.UserAttributesModuleAbstract;
import cz.metacentrum.perun.core.implApi.modules.attributes.UserAttributesModuleImplApi;

/**
 * Class for checking login uniqueness in the namespace and filling login value in namespace
 *
 * @author Michal Prochazka  &lt;michalp@ics.muni.cz&gt;
 * @author Slavek Licehammer &lt;glory@ics.muni.cz&gt;
 *
 * @date 3.6.2011 11:02:22
 */
public class urn_perun_user_attribute_def_def_login_namespace extends UserAttributesModuleAbstract implements UserAttributesModuleImplApi {

	private final static Logger log = LoggerFactory.getLogger(urn_perun_user_attribute_def_def_login_namespace.class);

	protected final static List<String> generatedNamespaces = ModulesUtilsBlImpl.getGeneratedLoginNamespaces();

	/**
	 * Checks if the user's login is unique in the namespace organization
	 *
	 * @param sess PerunSession
	 * @param user User to check attribute for
	 * @param attribute Attribute to check value to
	 * @throws cz.metacentrum.perun.core.api.exceptions.InternalErrorException
	 * @throws cz.metacentrum.perun.core.api.exceptions.WrongAttributeValueException
	 * @throws cz.metacentrum.perun.core.api.exceptions.WrongAttributeAssignmentException
	 */
	@Override
	public void checkAttributeValue(PerunSessionImpl sess, User user, Attribute attribute) throws InternalErrorException, WrongAttributeValueException, WrongAttributeAssignmentException {

		String userLogin = (String) attribute.getValue();
		if (userLogin == null) throw new WrongAttributeValueException(attribute, user, "Value can't be null");

		//Check attribute regex
		sess.getPerunBl().getModulesUtilsBl().checkAttributeRegex(attribute, "^[a-zA-Z0-9_][-A-z0-9_.@/]*$");
		//Check if user login is permitted or not permitted
		sess.getPerunBl().getModulesUtilsBl().checkUnpermittedUserLogins(attribute);

		// Get all users who have set attribute urn:perun:member:attribute-def:def:login-namespace:[login-namespace], with the value.
		List<User> usersWithSameLogin = sess.getPerunBl().getUsersManagerBl().getUsersByAttribute(sess, attribute);

		usersWithSameLogin.remove(user); //remove self
		if (!usersWithSameLogin.isEmpty()) {
			if(usersWithSameLogin.size() > 1) throw new ConsistencyErrorException("FATAL ERROR: Duplicated Login detected." +  attribute + " " + usersWithSameLogin);
			throw new WrongAttributeValueException(attribute, user, "This login " + attribute.getValue() + " is already occupied.");
		}

		try {
			sess.getPerunBl().getUsersManagerBl().checkReservedLogins(sess, attribute.getFriendlyNameParameter(), userLogin);
		} catch (AlreadyReservedLoginException ex) {
			throw new WrongAttributeValueException(attribute, user, "Login in specific namespace already reserved.", ex);
		}
	}

	/**
	 * Filling implemented for:
	 * - namespaces configured in /etc/perun/perun.properties as property: "perun.loginNamespace.generated"
	 * - You must create own attribute module for that namespace to define filling function
	 *
	 * @param perunSession PerunSession
	 * @param user User to fill attribute for
	 * @param attribute Attribute to fill value to
	 * @return Filled attribute
	 * @throws InternalErrorException
	 * @throws WrongAttributeAssignmentException
	 */
	@Override
	public Attribute fillAttribute(PerunSessionImpl perunSession, User user, AttributeDefinition attribute) throws InternalErrorException, WrongAttributeAssignmentException {

		Attribute filledAttribute = new Attribute(attribute);
		return filledAttribute;

	}

	/*public AttributeDefinition getAttributeDefinition() {
		AttributeDefinition attr = new AttributeDefinition();
		attr.setNamespace(AttributesManager.NS_USER_ATTR_DEF);
		attr.setFriendlyName("login-namespace");
		attr.setType(String.class.getName());
		attr.setDescription("Login namespace.");
		return attr;
	}*/
}
