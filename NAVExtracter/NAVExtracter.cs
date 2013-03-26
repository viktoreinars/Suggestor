using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.Collections;
using System.IO;
using Suggestor;

namespace NAVExtracter
{
    public class NAVExtracter
    {
        private NETConductor.Basics nc = new NETConductor.Basics();

        public Dictionary<string, SuggestorItem> GetItems(string dateFilter)
        {
            Dictionary<string, SuggestorItem> items = new Dictionary<string, SuggestorItem>();
            /*
            XmlNode generalLedgerEntries = nc.SelectFromTable("NCDEV.DT", 17, "*", "4=\"" + dateFilter + "\",5=\"2\",28=\"SALES\"", "");
            XmlNodeList generalLedgerLines = generalLedgerEntries.SelectNodes("Reply/G_L_Entry");

            foreach (XmlNode generalLedgerLine in generalLedgerLines)
            {
                string invoiceNo = ParseString(generalLedgerLine, "Document_No_");
            */
                XmlNode reply = nc.SelectFromTable("NCDEMO.DT", 113, "6,11,15", "131=\"" + dateFilter + "\"", "");
                XmlNodeList invoiceLines = reply.SelectNodes("Reply/Sales_Invoice_Line");
                foreach (XmlNode child in invoiceLines)
                {
                    string no = "";
                    XmlNode noNode = child.SelectSingleNode("No_");
                    if (noNode != null)
                        no = noNode.InnerText;
                    string description = "";
                    XmlNode descriptionNode = child.SelectSingleNode("Description");
                    if (descriptionNode != null)
                        description = descriptionNode.InnerText;
                    int quantity = 0;
                    XmlNode quantityNode = child.SelectSingleNode("Quantity");
                    if (quantityNode != null)
                        quantity = Int32.Parse(quantityNode.InnerText);
                    if (!items.ContainsKey(no))
                        items.Add(no, new Item(no, description, quantity));
                    ((Item)items[no]).SetQuantity(items[no].GetQuantity() + quantity);
                }
            //}

            return items;
        }

        public Tuple<Dictionary<string, User>, Dictionary<string, SuggestorInvoice>> GetInvoices(string dateFilter)
        {
            Dictionary<string, SuggestorInvoice> invoices = new Dictionary<string, SuggestorInvoice>();
            Dictionary<string, User> customers = new Dictionary<string, User>();
            /*
            XmlNode generalLedgerEntries = nc.SelectFromTable("NCDEV.DT", 17, "*", "4=\"" + dateFilter + "\",5=\"2\",28=\"SALES\"", "");
            XmlNodeList generalLedgerLines = generalLedgerEntries.SelectNodes("Reply/G_L_Entry");

            foreach (XmlNode generalLedgerLine in generalLedgerLines)
            {*/
                XmlNode reply = nc.SelectFromTable("NCDEMO.DT", 113, "2,3,6,11,15", "131=\"" + dateFilter + "\"", "");
                XmlNodeList invoiceLines = reply.SelectNodes("Reply/Sales_Invoice_Line");
            
                foreach (XmlNode child in invoiceLines)
                {
                    string customerId = ParseString(child, "Sell_to_Customer_No_");
                    string invoiceNo = ParseString(child, "Document_No_");

                    // Add customer if needed
                    if (!(customers.ContainsKey(customerId))) // customer has not been added
                        customers.Add(customerId, new User(customerId, customerId));

                    string no = ParseString(child, "No_");
                    string description = ParseString(child, "Description");
                    int quantity = ParseInt(child, "Quantity");

                    // Add Invoice
                    if (!(invoices.ContainsKey(invoiceNo))) // Invoice has not been added
                        invoices.Add(invoiceNo, new Invoice(invoiceNo, customerId));

                    // Add Item to Invoice
                    if (!(invoices[invoiceNo].GetInvoiceLines().ContainsKey(no)))
                        invoices[invoiceNo].GetInvoiceLines().Add(no, new InvoiceLine(no, description, quantity));
                    invoices[invoiceNo].GetInvoiceLines()[no].SetQuantity(invoices[invoiceNo].GetInvoiceLines()[no].GetQuantity() + 1); // Increment quantity                
                }

            //}

            foreach (string invoiceNo in invoices.Keys)
            {
                string customerId = invoices[invoiceNo].GetCustomerId();
                customers[customerId].PurchaseInvoices.Add((Invoice)invoices[invoiceNo]);
            }
            
            return Tuple.Create(customers, invoices);
        }

        private string ParseString(XmlNode node, string nodeName)
        {
            XmlNode valueNode = node.SelectSingleNode(nodeName);
            return valueNode.InnerText; // Dont handle null node
            /*if (invoiceNoNode != null)
                value = invoiceNoNode.InnerText;
            return value;*/
        }

        private int ParseInt(XmlNode node, string nodeName)
        {
            XmlNode valueNode = node.SelectSingleNode(nodeName);
            return Int32.Parse(valueNode.InnerText); // Dont handle null node
        }

        /*
        public string GenerateData(string dateFilter)
        {
            // ------ Retrieving data
            List<Item> items = GetItems(dateFilter);
            // Class label
            Item targetItem = items[0];
            items.Remove(targetItem);
            // Get data
            Dictionary<string, Dictionary<string, int>> invoices = GetInvoices(dateFilter);

            // ------ Writing data out to disk
            StreamWriter writer = File.CreateText(@"C:\Temp\NavData.txt");
            // Attributes
            writer.WriteLine("-Attributes");
            foreach (Item item in items)
            {
                writer.WriteLine(item.no + ",string");
            }
            // Class labels
            writer.WriteLine("-Class");
            writer.WriteLine("Bought,string");
            writer.WriteLine("NotBought,string");
            // Data
            writer.WriteLine("-Data");
            foreach (string invoiceNo in invoices.Keys)
            {
                string dataLine = "";
                foreach (Item item in items)
                {
                    if (invoices[invoiceNo].ContainsKey(item.no))
                        dataLine += "y,";
                    else
                        dataLine += "n,";
                }
                if (invoices[invoiceNo].ContainsKey(targetItem.no))
                    dataLine += "Bought";
                else
                    dataLine += "NotBought";
                writer.WriteLine(dataLine);
            }
            writer.Flush();
            writer.Close();
            return @"C:\Temp\NavData.txt";
        }*/
    }
}
