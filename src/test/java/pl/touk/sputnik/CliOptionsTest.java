package pl.touk.sputnik;

import org.apache.commons.cli.CommandLine;
import org.junit.Test;
import org.junit.Assert;
import pl.touk.sputnik.configuration.CliWrapper;

public class CliOptionsTest {

    private CliWrapper fixture = new CliWrapper();

    private static String SAMPLE_CONFIG = "/home/spoonman/sputnik/conf.properties";
    private static String SAMPLE_CHANGE_ID = "I0a2afb7ae4a94ab1ab473ba00e2ec7de381799a0";
    private static String SAMPLE_REVISION_ID = "3f37692af2290e8e3fd16d2f43701c24346197f0";
    private static String SAMPLE_PULL_REQUEST_ID = "123";

    @Test
    public void shouldExecuteGerritReview() throws Exception {
        // given
        String[] args = toArgs("-connector gerrit -conf %s -changeId %s -revisionId %s",
                SAMPLE_CONFIG, SAMPLE_CHANGE_ID, SAMPLE_REVISION_ID);

        // when
        CommandLine commandLine = fixture.parse(args);
        fixture.contextSensitiveValidation(commandLine);

        // then
        _(commandLine).hasOption(CliWrapper.CONNECTOR).withValue("gerrit");
        _(commandLine).hasOption(CliWrapper.CONF).withValue(SAMPLE_CONFIG);
        _(commandLine).hasOption(CliWrapper.CHANGE_ID).withValue(SAMPLE_CHANGE_ID);
        _(commandLine).hasOption(CliWrapper.REVISION_ID).withValue(SAMPLE_REVISION_ID);
    }

    @Test
    public void shouldExecuteStashReview() throws Exception {
        // given
        String[] args = toArgs("-connector stash -conf %s -pullRequestId %s", SAMPLE_CONFIG, SAMPLE_PULL_REQUEST_ID);

        // when
        CommandLine commandLine = fixture.parse(args);
        fixture.contextSensitiveValidation(commandLine);

        // then
        _(commandLine).hasOption(CliWrapper.CONNECTOR).withValue("stash");
        _(commandLine).hasOption(CliWrapper.PULL_REQUEST_ID).withValue(SAMPLE_PULL_REQUEST_ID);
    }

    private String[] toArgs(String argsFormat, String... substitutions) {
        return String.format(argsFormat, substitutions).split(" ");
    }

    private CliAssert _(CommandLine cli) {
        return new CliAssert(cli);
    }

    private static class CliAssert {

        CommandLine object;
        private String optionName;

        public CliAssert(CommandLine object) {
            this.object = object;
        }

        public CliAssert hasOption(String optionName) {
            Assert.assertTrue(object.hasOption(optionName));
            this.optionName = optionName;
            return this;
        }

        public CliAssert withValue(String value) {
            Assert.assertNotNull("Call hasOption first!", optionName);
            Assert.assertEquals(value, object.getOptionValue(optionName));
            return this;
        }
    }
}