package com.phoenixnap.oss.ramlapisync.generation.rules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.BeforeClass;
import org.junit.Test;

import com.phoenixnap.oss.ramlapisync.data.ApiResourceMetadata;
import com.phoenixnap.oss.ramlapisync.raml.InvalidRamlResourceException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

/**
 * @author aleksandars
 * @since 0.10.5
 */
public class Issue158RulesTest extends AbstractRuleTestBase {

	private Rule<JCodeModel, JDefinedClass, ApiResourceMetadata> rule;

	private static String LINE_END = System.getProperty("line.separator");

	@BeforeClass
	public static void initRaml() throws InvalidRamlResourceException {
		AbstractRuleTestBase.RAML = RamlLoader.loadRamlFromFile(AbstractRuleTestBase.RESOURCE_BASE + "issue-158.raml");
	}

	@Test
	public void applySpring4ControllerStubRule_shouldCreate_validCode() throws Exception {

		rule = new Spring4ControllerStubRule();
		rule.apply(getControllerMetadata(), jCodeModel);
		String removedSerialVersionUID = removeSerialVersionUID(serializeModel());
		verifyGeneratedCode("Issue158Spring4ControllerStub", removedSerialVersionUID);
	}

	private String removeSerialVersionUID(String serializedModel) throws IOException {

		BufferedReader bufReader = new BufferedReader(new StringReader(serializedModel));
		StringWriter stringWriter = new StringWriter();
		BufferedWriter bufWriter = new BufferedWriter(stringWriter);
		String line = null;
		while ((line = bufReader.readLine()) != null) {
			if (!line.contains("serialVersionUID = ")) {
				bufWriter.write(line + LINE_END);
			}
		}
		bufWriter.flush();
		bufWriter.close();
		return stringWriter.toString();
	}
}