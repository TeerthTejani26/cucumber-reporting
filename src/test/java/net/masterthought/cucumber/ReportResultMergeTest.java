package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Feature;
import org.junit.jupiter.api.Test;

import java.util.List;

import static net.masterthought.cucumber.reducers.ReducingMethod.MERGE_FEATURES_WITH_RETEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Expected conditions:
 * all-last-failed.json = p1.json + p2.json
 * all-last-failed.json = p1.json + p2.json + p2-rerun-failed.json
 * all-passed.json = p1.json + p2.json + p2-rerun-passed.json
 *
 *
 *
 * There are 2 features' files here:
 *
 * Num#1: ../reporting/categories.feature
 *
 * Feature: Product categories
 *  Background:
 *     Given Open Home page
 *
 *   Scenario: All categories are displayed on site
 *     When  Get list of existing products
 *     Then  List of categories is displayed on Home page and contains all values
 *
 *
 *
 * Num#2: ../reporting/home-page.feature
 * Feature: Home page
 *
 *   Scenario: Open Home page
 *     Given Open Home page
 */
class ReportResultMergeTest extends ReportGenerator {

    private static final String TIMESTAMPED = "timestamped/";
    private static final String ALL_FAILED = TIMESTAMPED + "all-last-failed.json";
    private static final String ALL_PASSED = TIMESTAMPED + "all-passed.json";
    private static final String PART_ONE = TIMESTAMPED + "part1.json";
    private static final String PART_TWO = TIMESTAMPED + "part2.json";
    private static final String PART2_RERUN_FAIL = TIMESTAMPED + "part2-rerun-failed.json";
    private static final String PART_TWO_RERUN_PASSED = TIMESTAMPED + "part2-rerun-passed.json";

    class AllInOneReport extends ReportGenerator {

        AllInOneReport(String reportName) {
            setUpWithJson(reportName);
        }
    }

    @Test
    void unsupportedReportFormat() {
        // given
        configuration.addReducingMethod(MERGE_FEATURES_WITH_RETEST);

        assertThrows(IllegalArgumentException.class, () -> {
            // when
            // then
            setUpWithJson(SAMPLE_FAILED_JSON, SAMPLE_JSON);
        });
    }

    @Test
    void checkAllFailedFileIsValidReport() {
        // given
        // when
        setUpWithJson(ALL_FAILED);

        // then
        assertThat(reportResult.getAllFeatures()).hasSize(2);
    }

    @Test
    void checkPartOneIsValidReport() {
        // given
        // when
        setUpWithJson(PART_ONE);
        // then
        assertThat(reportResult.getAllFeatures()).hasSize(1);
    }

    @Test
    void checkPartTwoIsValidReport() {
        // given
        // when
        setUpWithJson(PART_TWO);

        // then
        assertThat(reportResult.getAllFeatures()).hasSize(1);
    }

    @Test
    void parsePartOneTwo_WithFailedRerun() {
        // given
        configuration.addReducingMethod(MERGE_FEATURES_WITH_RETEST);
        setUpWithJson(PART_ONE, PART_TWO, PART2_RERUN_FAIL);

        // when
        Reportable current = reportResult.getFeatureReport();

        // then
        assertThat(reportResult.getAllFeatures()).hasSize(2);
        assertThat(current.getFailedScenarios()).isEqualTo(1);
    }

    @Test
    void merge_PartOneTwo_WithFailedRerun_Equals_AllInOneFailed() {
        // given
        configuration.addReducingMethod(MERGE_FEATURES_WITH_RETEST);
        setUpWithJson(PART_ONE, PART_TWO, PART2_RERUN_FAIL);
        ReportResult allInOneFailed = new AllInOneReport(ALL_FAILED).getReportResult();

        // when
        List<Feature> mergedResults = reportResult.getAllFeatures();

        // then
        assertThat(mergedResults)
                .usingElementComparator(new ReportResultSimpleFeatureComparator())
                .containsExactlyInAnyOrder(allInOneFailed.getAllFeatures().toArray(new Feature[]{}));
    }

    @Test
    void merge_PartOneTwo_WithPassedRerun_Equals_AllInOnePassed() {
        // given
        configuration.addReducingMethod(MERGE_FEATURES_WITH_RETEST);
        setUpWithJson(PART_ONE, PART_TWO, PART_TWO_RERUN_PASSED);
        ReportResult allInOnePassed = new AllInOneReport(ALL_PASSED).getReportResult();

        // when
        List<Feature> mergedResults = reportResult.getAllFeatures();

        // then
        assertThat(mergedResults)
                .usingElementComparator(new ReportResultSimpleFeatureComparator())
                .containsExactlyInAnyOrder(allInOnePassed.getAllFeatures().toArray(new Feature[]{}));
    }
}
