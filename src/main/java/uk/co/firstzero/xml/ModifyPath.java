/*
Copyright (c) 2011, 2012 Parjanya Mudunuri

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://opensource.org/licenses/mit-license.php
 */

package uk.co.firstzero.xml;

import org.apache.tools.ant.Task;

/**
 * Used for holding the xpath to be modified and the value it needs to be modified or deleted
 */
public class ModifyPath extends Task {
	private String path;
	private String value;
	private boolean delete;

    /**
     * Empty constructor
     */
	public ModifyPath() {}

    /**
     * Constructor of ModifyPath to modify a node based on xPATH
     * @param path xPATH to be modified
     * @param value The new value that must be set
     */
	public ModifyPath(String path, String value) {
		this.path = path;
		this.value = value;
	}

    /**
     * Constructor of ModifyPath to delete a node based on xPATH
     * @param path xPATH to be deleted
     * @param delete Set true to delete node
     */
	public ModifyPath(String path, boolean delete) {
		this.path = path;
		this.delete = delete;
	}

    /**
     * Sets the xPATH
     * @param path xPATH
     */
	public void setPath(String path) {
		this.path = path;
	}

    /**
     * Gets the xPATH
     * @return xPATH
     */
	public String getPath() {
		return path;
	}

    /**
     * Sets the new value
     * @param value Sets the new value
     */
	public void setValue(String value) {
		this.value = value;
	}

    /**
     * Gets the new value
     * @return Gets the new value
     */
	public String getValue() {
		return value;
	}

    /**
     * Sets the boolean value of whether the node is to be deleted
     * @param delete Sets the boolean value of whether the node is to be deleted
     */
	public void setDelete(boolean delete) {
		this.delete = delete;
	}

    /**
     * Returns the value of delete
     * @return Returns the value of delete
     */
	public boolean isDelete() {
		return delete;
	}
	
	public void execute() {}
}
