using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Mail;
using System.Text;
using System.Web;
using System.Web.Hosting;
using System.Web.Mvc;
using SeminarWeb.Models;

namespace SeminarWeb.Controllers
{
    public class RegisterController : Controller
    {
        SeminarEntities db = new SeminarEntities();

        // GET: Register
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Success()
        {
            return View();
        }

        public ActionResult SaveResponseData(FormCollection formData)
        {
            var maxID = db.T_USER_DATA.Max(n => n.ID);
            if (maxID != 0)
            {
                maxID++;
            }
            else
            {
                maxID = 1;
                //int v = Convert.ToInt32(DateTime.Now.ToString("yyyyMMdd") + "0001");
                //NewID = v;
            }

            T_USER_DATA t_USER_DATA = new T_USER_DATA()
            {
                ID = maxID,
                UserID = formData[0],
                Username = formData[1],
                Phone = formData[2],
                Email = formData[3],
                Password = formData[4]
            };

            

            //int rowLength = db.T_USER_DATA.Count(rowLength);
            //for(int i = 0; i < rowLength; i++)
            //{
            //    i = t_USER_DATA.ID;
            //}
            t_USER_DATA.Isvalid = false;

            db.T_USER_DATA.Add(t_USER_DATA);
            db.SaveChanges();
            BuildEmailTemplate(t_USER_DATA.ID);
            return RedirectToAction("Success");
        }

        public ActionResult Confirm(int regId)
        {
            ViewBag.regID = regId;
            return View();
        }

        public JsonResult RegisterConfirm(int regId)
        {
            T_USER_DATA Data = db.T_USER_DATA.Where(x => x.ID == regId).FirstOrDefault();
            Data.Isvalid = true;
            Data.ValidTime = DateTime.Now;
            db.SaveChanges();
            var msg = "Your Email Is Verified!";
            return Json(msg, JsonRequestBehavior.AllowGet);
        }

        public void BuildEmailTemplate(int regID)
        {
            string body = System.IO.File.ReadAllText(HostingEnvironment.MapPath("~/EmailTemplate/") + "Text" + ".cshtml");
            var regInfo = db.T_USER_DATA.Where(x => x.ID == regID).FirstOrDefault();
            var url = "https://localhost:44391/" + "Register/Confirm?regId=" + regID;
            body = body.Replace("@ViewBag.ConfirmationLink", url);
            body = body.ToString();
            BuildEmailTemplate("Your Account Is Successfully Created", body, regInfo.Email);
        }

        //Build Email For Sending
        public static void BuildEmailTemplate(string subjectText, string bodyText, string sendTo)
        {
            string from, to, bcc, cc, subject, body;
            from = "g8babe@gmail.com";
            to = sendTo.Trim();
            bcc = "";
            cc = "";
            subject = subjectText;
            StringBuilder sb = new StringBuilder();
            sb.Append(bodyText);
            body = sb.ToString();
            MailMessage mail = new MailMessage();
            mail.From = new MailAddress(from);
            mail.To.Add(new MailAddress(to));
            if (!string.IsNullOrEmpty(bcc))
            {
                mail.Bcc.Add(new MailAddress(bcc));
            }
            if (!string.IsNullOrEmpty(cc))
            {
                mail.CC.Add(new MailAddress(cc));
            }
            mail.Subject = subject;
            mail.Body = body;
            mail.IsBodyHtml = true;
            SendEmail(mail);
        }

        public static void SendEmail(MailMessage mail)
        {
            SmtpClient client = new SmtpClient();
            client.Host = "smtp.gmail.com";
            client.Port = 587;
            client.EnableSsl = true;
            client.UseDefaultCredentials = false;
            client.DeliveryMethod = SmtpDeliveryMethod.Network;
            client.Credentials = new System.Net.NetworkCredential("g8babe@gmail.com", "FJUG8G8be");
            try
            {
                client.Send(mail);
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        public JsonResult CheckValidUser(T_USER_DATA model)
        {
            string result = "Fail";
            var DataItem = db.T_USER_DATA.Where(x => x.UserID == model.UserID && x.Password == model.Password).SingleOrDefault();
            if (DataItem != null)
            {
                Session["ID"] = DataItem.ID.ToString();
                Session["UserID"] = DataItem.UserID.ToString();
                result = "Success";
            }
            return Json(result, JsonRequestBehavior.AllowGet);
        }

        public ActionResult AfterLogin()
        {
            if (Session["ID"] != null)
            {
                return View();
            }
            else
            {
                return RedirectToAction("Index");
            }
        }

        public ActionResult Logout()
        {
            Session.Clear();
            Session.Abandon();
            return RedirectToAction("Index");
        }

        // GET: Register/Details/5
        //public ActionResult Details(string id)
        //{
        //    if (id == null)
        //    {
        //        return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
        //    }
        //    T_USER_DATA t_USER_DATA = db.T_USER_DATA.Find(id);
        //    if (t_USER_DATA == null)
        //    {
        //        return HttpNotFound();
        //    }
        //    return View(t_USER_DATA);
        //}

        //// GET: Register/Create
        //public ActionResult Create()
        //{
        //    return View();
        //}

        //// POST: Register/Create
        //// 若要避免過量張貼攻擊，請啟用您要繫結的特定屬性。
        //// 如需詳細資料，請參閱 https://go.microsoft.com/fwlink/?LinkId=317598。
        //[HttpPost]
        //[ValidateAntiForgeryToken]
        //public ActionResult Create([Bind(Include = "UserID,Username,Phone,Email,Password,Isvalid,ValidTime")] T_USER_DATA t_USER_DATA)
        //{
        //    if (ModelState.IsValid)
        //    {
        //        db.T_USER_DATA.Add(t_USER_DATA);
        //        db.SaveChanges();
        //        return RedirectToAction("Index");
        //    }

        //    return View(t_USER_DATA);
        //}

        //// GET: Register/Edit/5
        //public ActionResult Edit(string id)
        //{
        //    if (id == null)
        //    {
        //        return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
        //    }
        //    T_USER_DATA t_USER_DATA = db.T_USER_DATA.Find(id);
        //    if (t_USER_DATA == null)
        //    {
        //        return HttpNotFound();
        //    }
        //    return View(t_USER_DATA);
        //}

        //// POST: Register/Edit/5
        //// 若要避免過量張貼攻擊，請啟用您要繫結的特定屬性。
        //// 如需詳細資料，請參閱 https://go.microsoft.com/fwlink/?LinkId=317598。
        //[HttpPost]
        //[ValidateAntiForgeryToken]
        //public ActionResult Edit([Bind(Include = "UserID,Username,Phone,Email,Password,Isvalid,ValidTime")] T_USER_DATA t_USER_DATA)
        //{
        //    if (ModelState.IsValid)
        //    {
        //        db.Entry(t_USER_DATA).State = EntityState.Modified;
        //        db.SaveChanges();
        //        return RedirectToAction("Index");
        //    }
        //    return View(t_USER_DATA);
        //}

        //// GET: Register/Delete/5
        //public ActionResult Delete(string id)
        //{
        //    if (id == null)
        //    {
        //        return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
        //    }
        //    T_USER_DATA t_USER_DATA = db.T_USER_DATA.Find(id);
        //    if (t_USER_DATA == null)
        //    {
        //        return HttpNotFound();
        //    }
        //    return View(t_USER_DATA);
        //}

        //// POST: Register/Delete/5
        //[HttpPost, ActionName("Delete")]
        //[ValidateAntiForgeryToken]
        //public ActionResult DeleteConfirmed(string id)
        //{
        //    T_USER_DATA t_USER_DATA = db.T_USER_DATA.Find(id);
        //    db.T_USER_DATA.Remove(t_USER_DATA);
        //    db.SaveChanges();
        //    return RedirectToAction("Index");
        //}

        //protected override void Dispose(bool disposing)
        //{
        //    if (disposing)
        //    {
        //        db.Dispose();
        //    }
        //    base.Dispose(disposing);
        //}
    }
}
