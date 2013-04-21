﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using Suggestor;

namespace MovieSuggestor
{
    public partial class User : SuggestorUser
    {

        string SuggestorUser.Id
        {
            get
            {
                return Id.ToString();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        [XmlIgnore]
        public Dictionary<string, SuggestorCollectionLine> CollectionLines
        {
            get
            {
                return Rating.ToDictionary(x => (x.Id), x => (SuggestorCollectionLine)x);

                //return Rating.Cast<SuggestorCollection>().ToList();
            }
            set
            {
                throw new NotImplementedException();
            }
        }
        // TODO: Ugly
        public List<Rating> Ratings
        {
            get
            {
                return Rating.ToList();
            }
        }
    }
}