package com.cumulocity.agent.packaging.microservice;

import com.google.common.collect.ImmutableList;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesExecution;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.google.common.base.Throwables.propagate;
import static java.nio.file.Files.createDirectories;
import static org.apache.commons.io.IOUtils.copyLarge;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PACKAGE;

@Mojo(name = "microservice-package", defaultPhase = PACKAGE)
public class MicroservicePackageMojo extends AbstractMojo {

    public static final String TARGET_FILENAME_PATTERN = "%s-package-%s.zip";
    
    @Parameter(defaultValue = "${project.build.directory}")
    private File build;
    @Parameter(property = "project.build.sourceEncoding", defaultValue = "utf8")
    private String encoding;
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;
    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession mavenSession;

    @Parameter(defaultValue = "true", property = "skip.microservice.package")
    private boolean skip;
    @Parameter(defaultValue = "${project.artifactId}")
    private String image;
    @Parameter(defaultValue = "${basedir}/src/main/configuration/cumulocity.json")
    private File manifestFile;

    @Component
    private MicroserviceDockerClient dockerClient;
    @Component
    private MavenResourcesFiltering mavenResourcesFiltering;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("skipping microservice package creation");
            return;
        }
        try {
            final String targetFilename = String.format(TARGET_FILENAME_PATTERN, project.getArtifactId(), project.getVersion());
            final File dockerImage = new File(build, "image.tar");
            createDirectories(build.toPath());
            dockerClient.saveDockerImage(image, dockerImage);

            final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(
                    new File(build, targetFilename)));
            addFileToZip(zipOutputStream, filterResourceFile(manifestFile));
            addFileToZip(zipOutputStream, dockerImage);
            zipOutputStream.close();

            dockerImage.delete();
        } catch (Exception e) {
            propagate(e);
        }
    }

    private File filterResourceFile(File source) throws IOException, MavenFilteringException {
        final File filteredDir = new File(build, File.separator + "filtered-resources");
        final MavenResourcesExecution execution = new MavenResourcesExecution(
                ImmutableList.of(resource(source)),
                filteredDir,
                project,
                encoding,
                ImmutableList.<String>of(),
                ImmutableList.<String>of(),
                mavenSession);
        createDirectories(filteredDir.toPath());
        execution.setOverwrite(true);
        mavenResourcesFiltering.filterResources(execution);
        return new File(filteredDir, File.separator + source.getName());
    }

    private Resource resource(File file) {
        Resource resource = new Resource();
        resource.setDirectory(file.getParent());
        resource.setFiltering(true);
        resource.setIncludes(ImmutableList.of(file.getName()));
        resource.setExcludes(ImmutableList.<String>of());
        return resource;
    }

    private void addFileToZip(final ZipOutputStream zipOutputStream, final File file) throws IOException {
        final FileInputStream input = new FileInputStream(file);
        final ZipEntry ze = new ZipEntry(file.getName());
        zipOutputStream.putNextEntry(ze);
        copyLarge(input, zipOutputStream);
        input.close();
        zipOutputStream.closeEntry();
    }

}
