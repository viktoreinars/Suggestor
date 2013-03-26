using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Suggestor;

namespace NAVExtracter
{
    public class User
    {
        private string id;
        private string name;
        private List<Invoice> purchaseInvoices;

        #region Getters/Setters

        public List<Invoice> PurchaseInvoices
        {
            get { return purchaseInvoices; }
            set { purchaseInvoices = value; }
        }

        public string Name
        {
            get { return name; }
            set { name = value; }
        }

        public string Id
        {
            get { return id; }
            set { id = value; }
        }       
  
        #endregion

        public User(string id, string name)
        {
            this.id = id;
            this.name = name;
            this.purchaseInvoices = new List<Invoice>();
        }

        public User(string id, string name, List<Invoice> purchaseInvoices)
        {
            this.id = id;
            this.name = name;
            this.purchaseInvoices = purchaseInvoices;
        }
    }
}
