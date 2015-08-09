//
//   iTunes skin creation tool for Mac :
//   Inject modified resource files into iTunes/Contens/Resource/iTunes.rsrc
//
//   https://github.com/Apophenic
//
//   Copyright (c) 2015 Justin Dayer (jdayer9@gmail.com)
//
//   Permission is hereby granted, free of charge, to any person obtaining a copy
//   of this software and associated documentation files (the "Software"), to deal
//   in the Software without restriction, including without limitation the rights
//   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//   copies of the Software, and to permit persons to whom the Software is
//   furnished to do so, subject to the following conditions:
//
//   The above copyright notice and this permission notice shall be included in
//   all copies or substantial portions of the Software.
//
//   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//   THE SOFTWARE.

package com.apophenic.itunesskinsmac;

import com.apophenic.rsrclib.*;

import java.io.*;
import java.nio.file.*;

public class Main
{
    public enum Operation
    {
        Extract, Inject, RestoreBackup
    }

    /** The operation to be performed in this instance (extract / inject) */
    private static Operation _op;

    /** @see <a href="https://github.com/Apophenic/rsrc-lib">_rsrc-lib</a> */
    private static RsrcFile _rsrc;

    /** Files will be extracted here, or all files in this dir will be injected */
    private static String _workingDir;

    private static boolean _isCreateBackup = true;

    public static void main(String[] args)
    {
	    readCmdArgs(args);

        switch (_op)
        {
            case Extract:
                System.out.println("Beginning Extraction Operation");
                extractResources();
                break;
            case Inject:
                System.out.println("Beginning Injection Operation");
                injectResources();
                break;
            case RestoreBackup:
                System.out.println("Restoring Backups...");
                restoreBackup();
                break;
            default:
                System.out.println("Unsupported Operation");
                break;
        }

        System.out.println("Operation finished.");
    }

    /** Extracts ALL resources from iTunes.rsrc */
    public static void extractResources()
    {
        ResourceLinkedList list = _rsrc.getResourceListByType(ResourceType.PNG);
        for (Resource res : list)
        {
            FileOutputStream fos = null;
            try
            {
                int id = res.getResourceID();
                File file = new File(_workingDir + "/" + id + ".png");
                if(!file.exists())
                {
                    file.createNewFile();
                }

                fos = new FileOutputStream(file);
                fos.write(_rsrc.loadResourceData(id, ResourceType.PNG));
                System.out.println("Extraction successful: " + id + ".png");
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Failed to find file");
            }
            catch (IOException e)
            {
                System.out.println("Failed to extract resources");
            }
            finally
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    System.out.println("Failed to close IO stream");
                }
            }
        }
    }

    /**
     * Injects all resources in workingdir into their respective
     * rsrc file (Images.rsrc or Images2.rsrc)
     */
    public static void injectResources()
    {
        for (File file : new File(_workingDir).listFiles())
        {
            try
            {
                byte[] data = new byte[(int) file.length()];
                int id = Integer.valueOf(getFileNameWithoutExtension(file.getName()));

                FileInputStream fis = new FileInputStream(file);
                fis.read(data);
                fis.close();

                if (_rsrc.getResourceListByType(ResourceType.PNG).contains(id))
                {
                    _rsrc.saveResourceData(data, id, ResourceType.PNG);
                    System.out.println("Injection successful: " + id + ".png");
                }
                else
                {
                    System.out.println("Invalid resource ID: " + id);
                }
            }
            catch (IOException e)
            {
                System.out.println("Error injecting resources");
            }


        }

        try
        {
            _rsrc.saveRsrcFile(_isCreateBackup);
        }
        catch (IOException e)
        {
            System.out.println("Failed to save updated file");
        }
    }

    public static void restoreBackup()
    {
        try
        {
            Path path = new File(_rsrc.getRsrcFile().getAbsolutePath() + ".bak").toPath();
            Files.copy(path, _rsrc.getRsrcFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.delete(path);
            System.out.println("Restore successful");
        }
        catch (IOException e)
        {
            System.out.println("Failed to restore files");
        }
    }

    public static void readCmdArgs(String[] args)
    {
        for (String arg : args)
        {
            String[] temp = arg.split("=");
            String flag = temp[0]; String value = temp[1];
            switch (flag)
            {
                case ("-op"):
                    if (value.equals("extract"))
                        _op = Operation.Extract;
                    else if (value.equals("inject"))
                        _op = Operation.Inject;
                    else if (value.equals("restore"))
                        _op = Operation.RestoreBackup;
                    break;
                case ("-itunesdir"):    // Typically is /Applications/iTunes.app
                    _rsrc = new RsrcFile(value + "/Contents/Resources/iTunes.rsrc");
                    break;
                case ("-workingdir"):
                    _workingDir = value.replace("\"", "");
                    break;
                case ("-createbackup"):
                    _isCreateBackup = Boolean.parseBoolean(value);
                    break;
                default:
                    System.out.println("Unsupported flag: " + flag);
                    break;
            }
        }
    }

    private static String getFileNameWithoutExtension(String name)
    {
        return name.substring(0, name.lastIndexOf('.'));
    }
}
