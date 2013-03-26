using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Suggestor;

namespace NAVExtracter
{
    public class Invoice : SuggestorInvoice
    {
        private string invoiceNo;
        private Dictionary<string, SuggestorInvoiceLine> invoiceLines;
        private string customerId;

        public string GetCustomerId()
        {
            return customerId;
        }

        public string GetInvoiceNo()
        {
            return invoiceNo;
        }

        public Dictionary<string, SuggestorInvoiceLine> GetInvoiceLines()
        {
            return invoiceLines;
        }

        public void SetCustomerId(string customerId)
        {
            this.customerId = customerId;
        }

        public void SetInvoiceNo(string invoiceNo)
        {
            this.invoiceNo = invoiceNo;
        }

        public void SetInvoiceLines(Dictionary<string, SuggestorInvoiceLine> invoiceLines)
        {
            this.invoiceLines = invoiceLines;
        }

        public Invoice(string invoiceNo, string userId)
        {
            this.invoiceNo = invoiceNo;
            this.customerId = userId;
            this.invoiceLines = new Dictionary<string, SuggestorInvoiceLine>();
        }

        public Invoice(string invoiceNo, string userId, Dictionary<string, SuggestorInvoiceLine> invoiceLines)
        {
            this.invoiceNo = invoiceNo;
            this.customerId = userId;
            this.invoiceLines = invoiceLines;
        }              
    }
}
