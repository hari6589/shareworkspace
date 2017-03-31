package com.core;

import static java.lang.System.out;

/**
 * Simple class demonstrating in-code access of package-specific information as
 * specified in the Manifest file.
 */
public class Main
{
   /**
    * Display (to standard output) package details for provided Package.
    *
    * @param pkg Package whose details need to be printed to standard output.
    */
   private void displayPackageDetails(final Package pkg)
   {
      final String name = pkg.getName();
      out.println(name);
      out.println("\tSpec Title/Version: " + pkg.getSpecificationTitle() + " " + pkg.getSpecificationVersion());
      out.println("\tSpec Vendor: " +  pkg.getSpecificationVendor());
      out.println("\tImplementation: " + pkg.getImplementationTitle() + " " + pkg.getImplementationVersion());
      out.println("\tImplementation Vendor: " + pkg.getImplementationVendor());
   }

   /**
    * Display all packages associated with the class loader that do not start
    * with "sun", "com", "java", or "org".
    */
   private void displayCustomPackage()
   {
      final Package[] packages = Package.getPackages();
      for (final Package pkg : packages)
      {
         final String name = pkg.getName();
         
         /*
         if (   !name.startsWith("sun") && !name.startsWith("java") && !name.startsWith("com") && !name.startsWith("org") ) {
            displayPackageDetails(pkg);
         }*/
         
         displayPackageDetails(pkg);
      }
   }

   /**
    * Main executable that demonstrates use of Package.getPackages() and
    * Package.getPackage(String).
    *
    * @param arguments Command-line arguments; none expected.
    */
   public static void main(String[] args) {
      final Main me = new Main();
      me.displayCustomPackage();
      //me.displayPackageDetails(Package.getPackage("dustin.examples"));
   }
}

